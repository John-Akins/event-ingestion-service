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

# Cloudflare
variable "cloudflare_ipv4_cidrs" {
  description = "Cloudflare IPv4 address ranges for security group ingress"
  type        = list(string)
  default     = [
    "173.245.48.0/20",
    "103.21.244.0/22",
    "103.22.200.0/22",
    "103.31.4.0/22",
    "141.101.64.0/18",
    "108.162.192.0/18",
    "190.93.240.0/20",
    "188.114.96.0/20",
    "197.234.240.0/22",
    "198.41.128.0/17",
    "162.158.0.0/15",
    "104.16.0.0/13",
    "104.24.0.0/14",
    "172.64.0.0/13",
    "131.0.0.0/16"
  ]
}
