resource "aws_db_subnet_group" "rds" {
  name       = "${var.service_name}-rds-subnet-group"
   subnet_ids = [
    aws_subnet.private_1.id,
    aws_subnet.private_2.id
  ]
}

resource "aws_db_instance" "rds" {
  identifier              = "${var.service_name}-db"
  engine                  = "postgres"
  engine_version          = "15.14"
  instance_class          = "db.t3.micro"
  allocated_storage       = 20
  username                = var.db_username
  password                = var.db_password
  db_name                 = var.db_name
  db_subnet_group_name    = aws_db_subnet_group.rds.name
  vpc_security_group_ids  = [aws_security_group.rds.id]
  skip_final_snapshot     = true
  publicly_accessible     = false

  tags = {
    Name = "${var.service_name}-rds"
  }
}
