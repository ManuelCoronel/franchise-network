resource "aws_secretsmanager_secret" "db_password" {
  name        = "${var.service_name}-db-password"
  description = "Database password for ${var.service_name}"

  tags = {
    Service = var.service_name
    Env     = "dev" # Valor fijo ya que solo tienes un ambiente
  }
}

resource "aws_secretsmanager_secret_version" "db_password_value" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = var.db_password
}