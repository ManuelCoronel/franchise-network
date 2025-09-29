terraform {
  required_version = ">= 1.0.0" # Versión mínima de Terraform
  required_providers {
    aws = {
      source  = "hashicorp/aws" # Proveedor de AWS oficial
      version = "~> 5.0"        # Versión estable
    }
  }
}

provider "aws" {
  region = var.aws_region
}