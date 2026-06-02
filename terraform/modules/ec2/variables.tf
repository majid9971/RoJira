# =============================================================
# EC2 Module – Variables
# =============================================================

variable "app_name" {
  description = "Name of the application"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC where the EC2 instance will be launched"
  type        = string
}

variable "subnet_id" {
  description = "ID of the public subnet for the EC2 instance"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t2.micro"
}

variable "ami_id" {
  description = "AMI ID for the EC2 instance. If empty, the latest Amazon Linux 2 AMI is used."
  type        = string
  default     = ""
}

variable "key_name" {
  description = "Name of the SSH key pair to associate with the instance (optional)"
  type        = string
  default     = ""
}

variable "office_ip_cidr" {
  description = "CIDR block of the office IP allowed to SSH (e.g. 203.0.113.50/32)"
  type        = string
}

variable "tags" {
  description = "Common resource tags"
  type        = map(string)
  default     = {}
}
