# =============================================================
# Terraform Variable Values (dev environment)
# =============================================================

app_name    = "java-calculator"
environment = "dev"
aws_region  = "ap-south-1"

# Networking
vpc_cidr           = "10.0.0.0/16"
availability_zones = ["ap-south-1a", "ap-south-1b"]
public_subnets     = ["10.0.1.0/24", "10.0.2.0/24"]
private_subnets    = ["10.0.11.0/24", "10.0.12.0/24"]

# Container
container_port = 8080
task_cpu       = 256
task_memory    = 512
desired_count  = 2

# EC2
ec2_instance_type = "t2.micro"
office_ip_cidr    = "0.0.0.0/32"  # Replace with your actual office IP CIDR

# Observability
log_retention_days = 30
