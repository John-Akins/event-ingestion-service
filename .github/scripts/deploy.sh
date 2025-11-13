#!/bin/bash
set -e

mkdir -p ~/.ssh
echo "$SSH_PRIVATE_KEY" > ~/.ssh/id_rsa
chmod 600 ~/.ssh/id_rsa
ssh-keyscan -H $EC2_IP >> ~/.ssh/known_hosts

echo "Deploying to EC2 instance at: $EC2_IP"
echo "Using RDS endpoint: "
if [ -n "$RDS_ENDPOINT" ]; then echo "::add-mask::$RDS_ENDPOINT"; fi
echo "and Docker Image Tag: "
if [ -n "$DOCKER_IMAGE" ]; then echo "::add-mask::$DOCKER_IMAGE"; fi

echo "deployment stage DOCKER_IMAGE:"
echo $DOCKER_IMAGE

# SSH access to EC2 instance
ssh -i ~/.ssh/id_rsa -o StrictHostKeyChecking=no ec2-user@$EC2_IP /bin/bash << EOF
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
AWS_RDS_HOSTNAME=$RDS_ENDPOINT
AWS_RDS_USERNAME=$AWS_RDS_USERNAME
AWS_RDS_PASSWORD=$AWS_RDS_PASSWORD
AWS_RDS_DB_NAME=$AWS_RDS_DB_NAME
AWS_RDS_PORT=$AWS_RDS_PORT"

  # Pull the latest Docker image
  docker pull $DOCKER_IMAGE

  # Stop and remove existing container if running
  docker stop event-ingestion-service || true
  docker rm event-ingestion-service || true

  # Run the new container
  docker compose up -d
  echo "Deployment completed successfully"
EOF