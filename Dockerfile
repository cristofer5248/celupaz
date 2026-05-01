# ETAPA 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Instalamos Node.js (JHipster lo necesita incluso en dev para el frontend)
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs

# Copiamos todo el proyecto
COPY . .

# Construimos el proyecto SIN el perfil -Pprod
# Esto generará un JAR que busca el perfil 'dev' por defecto
RUN ./mvnw clean package -DskipTests

# ETAPA 2: Ejecución
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando para arrancar forzando el perfil dev
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
