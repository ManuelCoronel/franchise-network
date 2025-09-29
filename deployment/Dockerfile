# Imagen base ligera con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR generado localmente
COPY  ../applications/app-service/build/libs/*.jar app.jar

# Variables de entorno por defecto (pueden sobrescribirse en ECS)
ENV JAVA_OPTS=" \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=70 \
  -Djava.security.egd=file:/dev/./urandom"



# Crear usuario seguro
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Ejecutar como usuario sin privilegios
USER appuser

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
