#!/bin/bash
set -e
# Validate required env variables
readonly required_vars=(EC2_IP DOCKER_IMAGE RDS_ENDPOINT DB_USERNAME DB_PASSWORD DB_NAME DB_PORT TF_VAR_REGION)

for var in "${required_vars[@]}"; do
  [[ -z "${!var}" ]] && { echo "❌ $var is required but not set"; exit 1; }
done

# Configure ssh access
mkdir -p ~/.ssh
echo "$SSH_PRIVATE_KEY" > ~/.ssh/id_rsa
chmod 600 ~/.ssh/id_rsa
ssh-keyscan -H "$EC2_IP" >> ~/.ssh/known_hosts

# Copy docker-compose.yml to EC2 instance
scp -i ~/.ssh/id_rsa docker-compose.yml ec2-user@$EC2_IP:~/

# SSH access to EC2 instance
ssh -i ~/.ssh/id_rsa ec2-user@$EC2_IP /bin/bash << EOF
  echo "Connected to EC2 instance"

  # Setup docker
  sudo dnf update -y
  sudo dnf install docker -y
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo usermod -a -G docker ec2-user
 
  # Install docker-compose if not available
  command -v docker-compose >/dev/null 2>&1 || {
    echo "Installing Docker Compose..."
    sudo curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
      -o /usr/local/bin/docker-compose && sudo chmod +x /usr/local/bin/docker-compose
  }

  # Create environment file using here string
  cat > .env <<< "
SPRING_PROFILES_ACTIVE=prod
DOCKER_IMAGE=$DOCKER_IMAGE
DB_HOSTNAME=$RDS_ENDPOINT
DB_USERNAME=$DB_USERNAME
DB_PASSWORD=$DB_PASSWORD
DB_NAME=$DB_NAME
DB_PORT=$DB_PORT"

  # Run docker commands with the docker group.
  echo "Pulling the latest Docker image..."
  sg docker -c "docker pull $DOCKER_IMAGE"

  # Stop and remove existing containers
  echo "Stopping existing containers..."
  sg docker -c "docker-compose down || true"

  # Create logs directory with proper permissions for container access
  sudo mkdir -p /logs
  sudo chmod a+w /logs

  # Run container and validate deployment
  echo "Starting the application..."
  sg docker -c "docker-compose up -d"

  # Quick health check with timeout and error handling
  sleep 30
  echo "Performing health check..."

  end_time=$((SECONDS+120))
  HEALTH_STATUS=0
  while [ $SECONDS -lt $end_time ]; do
    HEALTH_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:4040/manage/health || echo "0")
    if [ "$HEALTH_STATUS" -eq 200 ]; then
      echo "✅ Health check passed with status $HEALTH_STATUS"
      break
    fi
    echo "Health check failed with status $HEALTH_STATUS. Retrying in 5 seconds..."
    sleep 5
  done

  if [ "$HEALTH_STATUS" -ne 200 ]; then
    echo "❌ Deployment failed: Health check timed out after 2 minutes."
    echo "Final health status: $HEALTH_STATUS"
    echo "Container logs:"
    sg docker -c "docker-compose logs --tail=50 app"
    exit 1
  fi

  echo "✅ Deployment successful!"
EOF
