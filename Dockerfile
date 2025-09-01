# Build stage with Java 17
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# 3️⃣ Copy Maven wrapper and pom.xml first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 4️⃣ Make Maven wrapper executable
RUN chmod +x mvnw

# 5️⃣ Download dependencies (caches this layer)
RUN ./mvnw dependency:go-offline -B

# 6️⃣ Copy source code
COPY src ./src

# 7️⃣ Build the Spring Boot app (skip tests)
RUN ./mvnw clean package -DskipTests

# 8️⃣ Runtime stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 9️⃣ Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# 1️⃣0️⃣ Expose port
EXPOSE 8080

# 1️⃣1️⃣ Run as root (to access secret files without permission issues)
USER root

# 1️⃣2️⃣ Run the Spring Boot app and verify secret file access
ENTRYPOINT ["sh", "-c", "whoami && ls -l /etc/secrets/ca.jks && java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
