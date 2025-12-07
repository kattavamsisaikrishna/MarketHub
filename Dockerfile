# ===== Stage 1: Build the application =====
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (cache friendly)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn -q clean package -DskipTests


# ===== Stage 2: Run the application =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render will set $PORT; Spring Boot uses server.port=${PORT:8080}
ENV PORT=8080

# Expose the port (optional, mainly for documentation)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
