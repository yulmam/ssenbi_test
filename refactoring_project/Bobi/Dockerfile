# Base image: OpenJDK 17
FROM amazoncorretto:17-alpine-jdk

ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV

COPY ${JAR_FILE} app.jar

# Command to run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]

