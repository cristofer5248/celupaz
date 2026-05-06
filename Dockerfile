# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Instalar Node.js (Versión 20.x LTS es la recomendada para JHipster actual)
RUN apt-get update && \
    apt-get install -y ca-certificates curl gnupg && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    NODE_MAJOR=20 && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install nodejs -y && \
    apt-get clean

# Copiar código y dar permisos al wrapper de Maven
COPY . .
RUN chmod +x mvnw

# Empaquetar la aplicación con perfil dev
# Nota: Si vas a producción real, lo ideal sería -Pprod, 
# pero mantengo -Pdev por tu configuración de H2.
RUN ./mvnw clean package -DskipTests -Pdev

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto por defecto de JHipster
EXPOSE 8080

# Configuración optimizada para Render (512MB)
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-jar", "app.jar", "--spring.profiles.active=dev", "--server.address=0.0.0.0", "--server.port=8080"]
