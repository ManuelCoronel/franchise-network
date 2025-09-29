resource "aws_cloudwatch_log_group" "ecs" {
  name              = "/ecs/mi-microservicio" # Nombre del grupo
  retention_in_days = 7                       # Guarda logs por 7 días (opcional)
}