# Usamos una imagen de Java 17 o 21 (según lo que use tu JHipster)
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copiamos el archivo JAR generado
COPY target/*.jar app.jar

# Exponemos el puerto de JHipster
EXPOSE 8080

# Comando de ejecución forzando el perfil dev
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
