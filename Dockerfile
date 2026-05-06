# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Instalar Node.js 20.x de forma eficiente
RUN apt-get update && \
    apt-get install -y ca-certificates curl gnupg && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    NODE_MAJOR=20 && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install nodejs -y && \
    apt-get clean

# Cachear dependencias de Maven (acelera builds posteriores)
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY . .
RUN chmod +x mvnw

# Empaquetar para producción (-Pprod compila el frontend y lo inyecta en el JAR)
RUN ./mvnw clean package -DskipTests -Pprod

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Configuración de variables de entorno para la JVM
# -XX:+UseContainerSupport: Hace que la JVM respete los límites de Docker
# -XX:MaxRAMPercentage=75: Usa el 75% de los 512MB para el Heap (aprox 384MB)
ENV JAVA_OPTS="-Xms256m -Xmx384m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecución con perfil 'prod' y puerto dinámico de Render
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod --server.address=0.0.0.0 --server.port=${PORT:-8080}"]
