# Multi-stage Docker build for ReliefHub
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# If maven wrapper or mvn installed, or build jar directly
# We can also use a pre-built JAR or build inside container

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/reliefhub-1.0.0.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
