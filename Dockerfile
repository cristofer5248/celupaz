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

# 2. Copiar archivos del wrapper para descargar dependencias primero (Caché de capas)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 3. Descargar dependencias de Maven (Si no cambia el pom.xml, Docker usará el caché)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# 4. Copiar todo el código fuente del proyecto
COPY . .

# 5. IMPORTANTE: Volver a dar permisos de ejecución por si se perdieron al copiar
RUN chmod +x mvnw

# 6. Empaquetar para PRODUCCIÓN
# Esto compila Angular/React/Vue y genera el JAR optimizado
RUN ./mvnw clean package -DskipTests -Pprod

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 7. Configuración de memoria para los 512MB de Render
# Usamos MaxRAMPercentage para que Java se ajuste dinámicamente al contenedor
ENV JAVA_OPTS="-Xms256m -Xmx384m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# 8. Copiar el JAR generado desde el Stage de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto estándar
EXPOSE 8080

# 9. Comando de ejecución
# sh -c es necesario para que el sistema lea la variable de entorno $PORT de Render
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod --server.address=0.0.0.0 --server.port=${PORT:-8080}"]
