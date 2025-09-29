variable "aws_region" {
  default = "us-east-1"
}

variable "vpc_cidr" {
  default = "10.0.0.0/16"
}

variable "service_name" {
  default = "mi-microservicio"
}

variable "db_name" {
  default = "franchiseNetwork"
}

variable "db_username" {
  default = "postgres"
}

variable "db_password" {
  description = "Contraseña de la base de datos"
}

variable "ecr_repo_url" {
  description = "URL del ECR con la imagen Docker"
  type        = string
}

variable "vpc_id" {
  description = "ID de la VPC"
  type        = string
}

variable "vpc_cidr_block" {
  description = "CIDR de la VPC (por ejemplo 10.0.0.0/16)"
  type        = string
}

variable "public_subnets" {
  description = "Lista de subnets públicas para ECS"
  type        = list(string)
}

variable "private_subnets" {
  description = "Lista de subnets privadas para RDS"
  type        = list(string)
}

