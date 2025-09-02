# ---------- STAGE 1: Build ----------
FROM eclipse-temurin:17-jdk-jammy AS builder

# Set workdir
WORKDIR /app

# Copy Maven/Gradle wrapper and config first (better caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies (cached if unchanged)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Build the application JAR
RUN ./mvnw clean package -DskipTests


# ---------- STAGE 2: Run ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy only the built JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose Render's expected port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
