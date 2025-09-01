# 1️⃣ Build stage with Java 17
FROM eclipse-temurin:17-jdk-alpine AS build

# 2️⃣ Set working directory
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

# Install shadow package to modify users/groups
RUN apk add --no-cache shadow

WORKDIR /app

# 9️⃣ Add default app user to group 1000 (needed to access Render secret files)
RUN usermod -a -G 1000 nobody

# 10️⃣ Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# 11️⃣ Expose port
EXPOSE 8080

# 12️⃣ Set default user to nobody (non-root)
USER nobody

# 13️⃣ Run the Spring Boot app and verify secret file access
ENTRYPOINT ["sh", "-c", "whoami && ls -l /etc/secrets/ca.jks && java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
