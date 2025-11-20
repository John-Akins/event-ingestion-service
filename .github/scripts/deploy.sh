#!/bin/bash
set -e
# Validate required env variables
if [ -z "$EC2_IP" ]; then echo "EC2_IP is empty or not set"; exit 1; fi
if [ -z "$DOCKER_IMAGE" ]; then echo "DOCKER_IMAGE is empty or not set"; exit 1; fi
if [ -z "$RDS_ENDPOINT" ]; then echo "RDS_ENDPOINT is empty or not set"; exit 1; fi
if [ -z "$AWS_RDS_USERNAME" ]; then echo "AWS_RDS_USERNAME is empty or not set"; exit 1; fi
if [ -z "$AWS_RDS_PASSWORD" ]; then echo "AWS_RDS_PASSWORD is empty or not set"; exit 1; fi
if [ -z "$AWS_RDS_DB_NAME" ]; then echo "AWS_RDS_DB_NAME is empty or not set"; exit 1; fi
if [ -z "$AWS_RDS_PORT" ]; then echo "AWS_RDS_PORT is empty or not set"; exit 1; fi
if [ -z "$TF_VAR_REGION" ]; then echo "TF_VAR_REGION is empty or not set"; exit 1; fi

# Configure ssh access
mkdir -p ~/.ssh
echo "$SSH_PRIVATE_KEY" > ~/.ssh/id_rsa
chmod 600 ~/.ssh/id_rsa
ssh-keyscan -H "$EC2_IP" >> ~/.ssh/known_hosts

# Copy docker-compose.yml to EC2 instance
scp -i ~/.ssh/id_rsa docker-compose.yml ec2-user@$EC2_IP:~/

echo "Deploying to EC2 instance at: $EC2_IP"
echo "Using RDS endpoint: $RDS_ENDPOINT"
echo "and Docker Image: $DOCKER_IMAGE"
echo "Region: $TF_VAR_REGION"

# SSH access to EC2 instance
ssh -i ~/.ssh/id_rsa ec2-user@$EC2_IP /bin/bash << EOF
  echo "Connected to EC2 instance"

  # Setup docker
  sudo dnf update -y
  sudo dnf install docker -y
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo usermod -a -G docker ec2-user

  # Setup docker-compose
  sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose

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

  # Stop and remove existing containers
  docker-compose down || true

  # Create logs directory with proper permissions for container access
  mkdir -p logs
  chmod a+w logs

  # Run container
  docker-compose up -d

  # # Wait for container to start
  sleep 10

  # Validate that the app service is running
  if ! docker-compose ps app | grep -q "Up"; then
    echo "Error: Docker container for app service is not running"
    exit 1
  fi

  echo "Deployment completed successfully"
EOF
