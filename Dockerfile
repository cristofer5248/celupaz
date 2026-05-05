# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Instalar Node.js (JHipster lo necesita para los assets estáticos)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

COPY . .
RUN chmod +x mvnw

# Forzamos el perfil dev en el empaquetado para asegurar los drivers
RUN ./mvnw clean package -DskipTests -Pdev

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el JAR (JHipster suele generar solo uno en target)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Ajustes de memoria para evitar que la JVM consuma los 1GB de golpe y el OOM Killer la mate
ENTRYPOINT ["java", "-Xmx768m", "-jar", "app.jar", "--spring.profiles.active=dev", "--server.address=0.0.0.0"]
