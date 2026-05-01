# STAGE 1: Build using Maven and JDK 21
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Install Node.js (Required for JHipster frontend assets)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean

# Copy the source code
COPY . .

# Ensure the wrapper has execution permissions
RUN chmod +x mvnw

# Build the JAR (using skipTests to speed up deployment)
RUN ./mvnw clean package -DskipTests

# STAGE 2: Execution using JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run with dev profile and bind to 0.0.0.0 for Fly.io reachability
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev", "--server.address=0.0.0.0"]
