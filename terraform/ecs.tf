# Cluster ECS
resource "aws_ecs_cluster" "main" {
  name = "${var.service_name}-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "mi-microservicio-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"

  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn 

  container_definitions = jsonencode([
    {
      name      = "mi-microservicio-container",
      image     = "${aws_ecr_repository.app.repository_url}:latest",
      essential = true,
      portMappings = [
        {
          containerPort = 8080,
          hostPort      = 8080
        }
      ],
      environment = [
        {
          name  = "SPRING_DATASOURCE_USERNAME",
          value = var.db_username
        },
        {
          name  = "SPRING_DATASOURCE_HOST",
          value = "${aws_db_instance.rds.address}"
        },
        {
          name  = "SPRING_DATASOURCE_DB_NAME",
          value = var.db_name
        }
      ],
            secrets = [
        {
          name      = "SPRING_DATASOURCE_PASSWORD",
          valueFrom = aws_secretsmanager_secret.db_password.arn

        }],
      logConfiguration = {
        logDriver = "awslogs"
        options = {
        awslogs-group         = aws_cloudwatch_log_group.ecs.name
        awslogs-region        = var.aws_region
        awslogs-stream-prefix = "ecs"
  }
}
    }
  ])
}


resource "aws_ecs_service" "app" {
  name            = "${var.service_name}-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

network_configuration {
  subnets          = [aws_subnet.public_1.id, aws_subnet.public_2.id] 
  security_groups  = [aws_security_group.ecs_tasks.id]
  assign_public_ip = true
}
  depends_on = [
    aws_ecs_cluster.main,
    aws_ecs_task_definition.app,
    aws_db_instance.rds
  ]
}