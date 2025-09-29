output "db_endpoint" {
  value = aws_db_instance.rds.address
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}