# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# 1. Instalar Node.js (Necesario para compilar el frontend de JHipster)
RUN apt-get update && \
    apt-get install -y ca-certificates curl gnupg && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    NODE_MAJOR=20 && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install nodejs -y && \
    apt-get clean

# 2. Copiar archivos necesarios para descargar dependencias (Aprovecha el caché de capas)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw

# 3. Descargar dependencias de Maven (Esto se saltará en futuros builds si no cambias el pom.xml)
RUN ./mvnw dependency:go-offline -B

# 4. Copiar el resto del código fuente y compilar para PRODUCCIÓN
# El perfil -Pprod compila el frontend (Angular/React/Vue) y lo mete en el JAR
COPY . .
RUN ./mvnw clean package -DskipTests -Pprod

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 5. Optimización de Memoria para los 512MB de Render
# Usamos MaxRAMPercentage para que la JVM se ajuste automáticamente al contenedor
ENV JAVA_OPTS="-Xms256m -Xmx384m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# 6. Copiar solo el artefacto final desde el stage de Build
COPY --from=build /app/target/*.jar app.jar

# Informar el puerto (Render detectará esto automáticamente)
EXPOSE 8080

# 7. Ejecución final
# Se usa 'sh -c' para que la variable $PORT de Render funcione correctamente
# Se activa el perfil 'prod' para usar MySQL según tu application-prod.yml
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod --server.address=0.0.0.0 --server.port=${PORT:-8080}"]
