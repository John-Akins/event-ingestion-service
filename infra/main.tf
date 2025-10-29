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

# CREATE SUBNET GROUP
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "rds-subnet-group"
  subnet_ids = [var.subnet_a, var.subnet_b]
}

# CREATE SECURITY GROUP
resource "aws_security_group" "ec2_sg" {
  name   = "ec2-sg"
  vpc_id = var.default_vpc

  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    to_port     = 5432
    from_port   = 5432
  }
  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    to_port     = 22
    from_port   = 22
  }
  ingress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "tcp"
    to_port     = 8085
    from_port   = 8085
  }
  egress {
    cidr_blocks = ["0.0.0.0/0"]
    protocol    = "-1" #all protocols
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
  public_key = var.ssh_private_key
}

resource "aws_instance" "eis_ec2" {
  ami                         = var.ec2_ami
  instance_type               = var.ec2_instance_type
  vpc_security_group_ids      = [aws_security_group.ec2_sg.id]
  key_name                    = aws_key_pair.eis_ec2_key.key_name
  associate_public_ip_address = true
}

# Output

output "eis_ec2_ip" {
  value = aws_instance.eis_ec2.public_ip
}
