# =============================================================
# Outputs
# =============================================================

output "vpc_id" {
  description = "ID of the VPC"
  value       = module.networking.vpc_id
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = module.networking.public_subnet_ids
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = module.networking.private_subnet_ids
}

output "ecr_repository_url" {
  description = "URL of the ECR repository"
  value       = module.ecr.repository_url
}

output "ecr_repository_arn" {
  description = "ARN of the ECR repository"
  value       = module.ecr.repository_arn
}

output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = module.ecs.cluster_name
}

output "ecs_service_name" {
  description = "Name of the ECS service"
  value       = module.ecs.service_name
}

output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer"
  value       = module.ecs.alb_dns_name
}

output "alb_url" {
  description = "Full URL of the application via the ALB"
  value       = "http://${module.ecs.alb_dns_name}"
}

output "cloudwatch_log_group" {
  description = "CloudWatch log group name for the application"
  value       = aws_cloudwatch_log_group.app.name
}

# ── EC2 Outputs ──────────────────────────────────────────────
output "ec2_instance_id" {
  description = "ID of the EC2 instance"
  value       = module.ec2.instance_id
}

output "ec2_public_ip" {
  description = "Public IP address of the EC2 instance"
  value       = module.ec2.public_ip
}

output "ec2_private_ip" {
  description = "Private IP address of the EC2 instance"
  value       = module.ec2.private_ip
}

output "ec2_security_group_id" {
  description = "Security group ID for the EC2 instance"
  value       = module.ec2.security_group_id
}

output "ec2_iam_role_arn" {
  description = "ARN of the EC2 IAM role"
  value       = module.ec2.iam_role_arn
}
