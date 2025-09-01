# Build stage with Java 17
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml for caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the Spring Boot app (skip tests)
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built fat JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the embedded ca.jks into image
COPY ca.jks /app/ca.jks

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot app as root (can adjust user later for production)
USER root

# Verify user and presence of ca.jks and then run the app with truststore property
ENTRYPOINT ["sh", "-c", "whoami && ls -l /app/ca.jks && java -Dserver.port=${PORT:-8080} -Djavax.net.ssl.trustStore=/app/ca.jks -Djavax.net.ssl.trustStorePassword=12345678 -Djavax.net.ssl.trustStoreType=JKS -jar /app/app.jar"]
