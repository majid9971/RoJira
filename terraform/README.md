# Terraform Infrastructure – Java Basic Calculator

This directory contains Terraform code to provision the full AWS infrastructure for the **Java Basic Calculator** application.

## Architecture Overview

```
Internet
   │
   ▼
Application Load Balancer (Public Subnets)
   │
   ▼
ECS Fargate Tasks (Private Subnets)
   │
   ├── ECR (Container Registry)
   ├── CloudWatch Logs
   └── Auto Scaling (CPU & Memory based)
```

### Resources Provisioned

| Resource | Description |
|---|---|
| **VPC** | Isolated network with public & private subnets across 2 AZs |
| **Internet Gateway** | Allows public internet access |
| **NAT Gateways** | Allow private subnet egress (one per AZ for HA) |
| **Security Groups** | ALB (port 80/443) and ECS tasks (port 8080) |
| **ECR Repository** | Stores Docker images with lifecycle policies |
| **ECS Cluster** | Fargate-based cluster with Container Insights |
| **ECS Task Definition** | Fargate task with 256 CPU / 512 MiB memory |
| **ECS Service** | Runs 2 replicas with deployment circuit breaker |
| **ALB** | Application Load Balancer with HTTP listener |
| **Auto Scaling** | Scales tasks on CPU (>70%) and Memory (>80%) |
| **CloudWatch Logs** | Log group with 30-day retention |

## Directory Structure

```
terraform/
├── main.tf               # Root module – wires all sub-modules together
├── variables.tf          # Input variable declarations
├── outputs.tf            # Root outputs
├── providers.tf          # AWS provider & Terraform version constraints
├── terraform.tfvars      # Default variable values (dev environment)
└── modules/
    ├── networking/       # VPC, subnets, IGW, NAT, route tables, SGs
    │   ├── main.tf
    │   ├── variables.tf
    │   └── outputs.tf
    ├── ecr/              # ECR repository + lifecycle policy
    │   ├── main.tf
    │   ├── variables.tf
    │   └── outputs.tf
    └── ecs/              # ECS cluster, task def, service, ALB, autoscaling
        ├── main.tf
        ├── variables.tf
        └── outputs.tf
```

## Prerequisites

- [Terraform](https://developer.hashicorp.com/terraform/downloads) >= 1.5.0
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html) configured with appropriate credentials
- [Docker](https://docs.docker.com/get-docker/) for building and pushing the container image

## Deployment Steps

### 1. Authenticate with AWS

```bash
aws configure
# or use environment variables:
export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
export AWS_DEFAULT_REGION=us-east-1
```

### 2. Build & Push Docker Image to ECR

```bash
# Get ECR login token
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Build image
docker build -t java-calculator .

# Tag & push
docker tag java-calculator:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/java-calculator-dev:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/java-calculator-dev:latest
```

### 3. Initialise Terraform

```bash
cd terraform
terraform init
```

### 4. Review the Plan

```bash
terraform plan -var-file="terraform.tfvars"
```

### 5. Apply

```bash
terraform apply -var-file="terraform.tfvars"
```

### 6. Access the Application

After apply completes, retrieve the ALB URL:

```bash
terraform output alb_url
```

## Environments

To deploy to a different environment, override the `environment` variable:

```bash
terraform apply -var="environment=prod" -var="desired_count=4" -var="task_cpu=512" -var="task_memory=1024"
```

## Tear Down

```bash
terraform destroy -var-file="terraform.tfvars"
```
