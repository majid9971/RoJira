# =============================================================
# Main Terraform Configuration
# Java Basic Calculator Application - AWS Infrastructure
# =============================================================

locals {
  app_name    = var.app_name
  environment = var.environment
  common_tags = {
    Application = var.app_name
    Environment = var.environment
    ManagedBy   = "Terraform"
    Project     = "ROJ-56"
  }
}

# ── Networking ──────────────────────────────────────────────
module "networking" {
  source = "./modules/networking"

  app_name           = local.app_name
  environment        = local.environment
  vpc_cidr           = var.vpc_cidr
  availability_zones = var.availability_zones
  public_subnets     = var.public_subnets
  private_subnets    = var.private_subnets
  tags               = local.common_tags
}

# ── ECR Repository ──────────────────────────────────────────
module "ecr" {
  source = "./modules/ecr"

  app_name    = local.app_name
  environment = local.environment
  tags        = local.common_tags
}

# ── ECS Cluster & Service ────────────────────────────────────
module "ecs" {
  source = "./modules/ecs"

  app_name            = local.app_name
  environment         = local.environment
  vpc_id              = module.networking.vpc_id
  private_subnet_ids  = module.networking.private_subnet_ids
  public_subnet_ids   = module.networking.public_subnet_ids
  alb_security_group  = module.networking.alb_security_group_id
  ecs_security_group  = module.networking.ecs_security_group_id
  ecr_repository_url  = module.ecr.repository_url
  container_port      = var.container_port
  cpu                 = var.task_cpu
  memory              = var.task_memory
  desired_count       = var.desired_count
  tags                = local.common_tags
}

# ── CloudWatch Log Group ─────────────────────────────────────
resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/${local.app_name}-${local.environment}"
  retention_in_days = var.log_retention_days
  tags              = local.common_tags
}
