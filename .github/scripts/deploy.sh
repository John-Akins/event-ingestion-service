#!/bin/bash
set -e
# Validate required env variables
readonly required_vars=(EC2_IP DOCKER_IMAGE RDS_ENDPOINT AWS_RDS_USERNAME AWS_RDS_PASSWORD AWS_RDS_DB_NAME AWS_RDS_PORT TF_VAR_REGION)

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

echo "🚀 Deploying $DOCKER_IMAGE to $EC2_IP (RDS: $RDS_ENDPOINT, Region: $TF_VAR_REGION)"

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
AWS_RDS_HOSTNAME=$RDS_ENDPOINT
AWS_RDS_USERNAME=$AWS_RDS_USERNAME
AWS_RDS_PASSWORD=$AWS_RDS_PASSWORD
AWS_RDS_DB_NAME=$AWS_RDS_DB_NAME
AWS_RDS_PORT=$AWS_RDS_PORT"

  # Pull the latest Docker image
  docker pull $DOCKER_IMAGE

  # Stop and remove existing containers (for clean deployment)
  echo "Stopping existing containers..."
  docker-compose down || true

  # Create logs directory with proper permissions for container access
  mkdir -p /logs
  sudo chown -R 1000:1000 /logs
  sudo chmod -R 755 /logs

  # Run container and validate deployment
  docker-compose up -d

  # Quick health check with timeout and error handling
  sleep 30
  echo "Performing health check..."
  if HEALTH_STATUS=$(curl -s --max-time 30 -o /dev/null -w "%{http_code}" http://localhost:4040/manage/health 2>/dev/null); then
    if [[ "$HEALTH_STATUS" =~ ^[0-9]+$ ]] && [ "$HEALTH_STATUS" -eq 200 ]; then
      echo "✅ Deployment successful - Health check passed"
    else
      echo "❌ Deployment failed - Health check failed with status $HEALTH_STATUS"
      docker-compose logs app
      exit 1
    fi
  else
    echo "❌ Deployment failed - Could not reach health endpoint"
    docker-compose logs app
    exit 1
  fi
EOF
