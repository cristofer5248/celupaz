# ETAPA 1: Construcción usando Debian para compatibilidad
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Instalar Node.js y herramientas necesarias en Debian
RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

# Copiar el código
COPY . .

# Dar permisos al wrapper si es necesario
RUN chmod +x mvnw

# Construir el JAR (sin perfil prod para mantener modo dev)
RUN ./mvnw clean package -DskipTests

# ETAPA 2: Ejecución
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Forzamos el perfil dev y nos aseguramos de que escuche en todas las interfaces
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev", "--server.address=0.0.0.0"]
