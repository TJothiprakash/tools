# ---------- Stage 1: Build the Spring Boot app ----------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run the Spring Boot app ----------
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/target/tools-backend.jar app.jar

# Expose port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
