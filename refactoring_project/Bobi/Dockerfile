# Base image: OpenJDK 17
FROM amazoncorretto:17-alpine-jdk

ARG JAR_FILE=target/*.jar
ARG PROFILES
ARG ENV

# Set working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container at /app
COPY build/libs/Bobi-0.0.1-SNAPSHOT.jar /app/app.jar
COPY .env /app/
COPY ${JAR_FILE} app.jar


ENV TZ="Asia/Seoul"

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]

