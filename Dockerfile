# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Instalar Node.js (Necesario para compilar el frontend de JHipster)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://nodesource.com | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

# Copiar código y dar permisos al wrapper de Maven
COPY . .
RUN chmod +x mvnw

# Empaquetar la aplicación con perfil dev (incluye H2 y recursos de desarrollo)
RUN ./mvnw clean package -DskipTests -Pdev

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Informar el puerto (JHipster usa 8080 por defecto)
EXPOSE 8080

# Configuración de ejecución optimizada para los 512MB de Render:
# -Xmx384m: Límite máximo de memoria para la JVM (deja espacio para el SO)
# --spring.profiles.active=dev: Activa el perfil de desarrollo
# --server.address=0.0.0.0: Permite conexiones externas
# --server.port=8080: Forzamos el puerto 8080
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-jar", "app.jar", "--spring.profiles.active=dev", "--server.address=0.0.0.0", "--server.port=8080"]
