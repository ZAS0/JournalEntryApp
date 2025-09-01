# Use Java 17 JDK from Eclipse Temurin
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot application (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests

# Copy the built jar to a standard location
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

# Expose the port your app will run on
EXPOSE 8080

# Set the entrypoint to run the Spring Boot app
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
