terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~>4.19.0"
    }
  }

  backend "s3" {
    bucket = "eis-terraform-state-0280"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = var.region
}

# Resources

# Create Subnet Group
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "rds-subnet-group"
  subnet_ids = [var.subnet_a, var.subnet_b]
}

# Create Security Group
resource "aws_security_group" "ec2_sg" {
  name   = "ec2-sg"
  vpc_id = var.default_vpc

  # Database access
  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    to_port     = 5432
    from_port   = 5432
  }

  # SSH access
  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    to_port     = 22
    from_port   = 22
  }

  # Direct access from ALB to Spring Boot port
  ingress {
    from_port   = 8085
    to_port     = 8085
    protocol    = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  # Health check access from ALB
  ingress {
    from_port       = 4040
    to_port         = 4040
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "-1" #all protocols
    to_port     = 0
    from_port   = 0
  }
}

# ALB Security Group for Cloudflare proxy
resource "aws_security_group" "alb_sg" {
  name   = "alb-sg"
  vpc_id = var.default_vpc

  # Allow traffic from Cloudflare IP ranges (IPv4 only to avoid AWS SG limitations)
  ingress {
    cidr_blocks = var.cloudflare_ipv4_cidrs
    protocol    = "tcp"
    to_port     = 80
    from_port   = 80
  }
  ingress {
    cidr_blocks = var.cloudflare_ipv4_cidrs
    protocol    = "tcp"
    to_port     = 443
    from_port   = 443
  }
  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "-1"
    to_port     = 0
    from_port   = 0
  }
}

resource "aws_db_instance" "primary" {
  identifier             = var.rdsconfig_identifier             
  engine                 = var.rdsconfig_engine                 
  engine_version         = var.rdsconfig_engine_version         
  instance_class         = var.rdsconfig_instance_class         
  allocated_storage      = var.rdsconfig_allocated_storage      
  storage_type           = var.rdsconfig_storage_type           
  db_name                = var.rdsconfig_db_name                
  username               = var.rdsconfig_username               
  password               = var.rdsconfig_password               
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  multi_az               = false
  publicly_accessible    = true
  skip_final_snapshot    = true
}

resource "aws_key_pair" "eis_ec2_key" {
  key_name   = "eis-ec2-key"
  public_key = var.ssh_public_key
}

resource "aws_instance" "eis_ec2" {
  ami                         = var.ec2_ami
  instance_type               = var.ec2_instance_type
  vpc_security_group_ids      = [aws_security_group.ec2_sg.id]
  key_name                    = aws_key_pair.eis_ec2_key.key_name
  associate_public_ip_address = true
}

# Application Load Balancer for SSL termination
resource "aws_lb" "app_alb" {
  name               = "app-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [var.subnet_a, var.subnet_b]

  enable_deletion_protection = false
}

# Target Group for ALB
resource "aws_lb_target_group" "app_tg" {
  name     = "app-tg"
  port     = 8085
  protocol = "HTTP"
  vpc_id   = var.default_vpc

  health_check {
    path                = "/manage/health"
    port                = "4040"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 30
    timeout             = 10
    healthy_threshold   = 3
    unhealthy_threshold = 3
  }
}

# Register EC2 instance with target group
resource "aws_lb_target_group_attachment" "app_attachment" {
  target_group_arn = aws_lb_target_group.app_tg.arn
  target_id        = aws_instance.eis_ec2.id
  port             = 8085
}

# HTTP Listener - SSL handled by Cloudflare upstream
resource "aws_lb_listener" "app_http" {
  load_balancer_arn = aws_lb.app_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

# Output

output "eis_ec2_ip" {
  value = aws_instance.eis_ec2.public_ip
}

output "alb_dns_name" {
  value = aws_lb.app_alb.dns_name
}

output "rds_endpoint" {
  value = aws_db_instance.primary.endpoint
}
