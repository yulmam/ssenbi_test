# Base image: OpenJDK 17
FROM amazoncorretto:17-alpine-jdk

ARG JAR_FILE=target/*.jar
ARG PROFILES
ARG ENV

# Set working directory inside the container
WORKDIR /app

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]

