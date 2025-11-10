# Variables

variable "region" {
  type    = string
}

# VPC
variable "default_vpc" {
  type    = string
}
variable "subnet_a" {
  type    = string
}
variable "subnet_b" {
  type    = string
}

# RDS
variable "rdsconfig_identifier" {
  type = string
}
variable "rdsconfig_engine" { 
  type = string
}
variable "rdsconfig_engine_version" { 
  type = string
}
variable "rdsconfig_instance_class" { 
  type = string
}
variable "rdsconfig_allocated_storage" {
  type = number
}
variable "rdsconfig_storage_type" { 
  type = string
}
variable "rdsconfig_db_name" { 
  type = string
}
variable "rdsconfig_username" { 
  type = string
}
variable "rdsconfig_password" { 
  type = string
}

# EC2
variable "ec2_ami" {
  type    = string
}
variable "ec2_instance_type" {
  type = string
}

variable "ssh_public_key" {
  type      = string
  sensitive = true
}

