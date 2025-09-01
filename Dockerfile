# 1️Base image with Java 17 JDK
FROM eclipse-temurin:17-jdk-alpine AS build

# 2️ Set working directory
WORKDIR /app

# 3️ Copy only Maven wrapper and pom.xml first
# This allows caching dependencies separately
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 4️ Make Maven wrapper executable
RUN chmod +x mvnw

# 5️ Download dependencies (caches this layer)
RUN ./mvnw dependency:go-offline -B

# 6️ Copy source code
COPY src ./src

# 7️ Build the Spring Boot app (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# 8️ Use a smaller image for runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 9️ Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

#  Expose port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
