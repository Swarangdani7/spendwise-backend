# ================================
# 1. Build Stage
# ================================
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (faster builds)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the full project and build
COPY src ./src
RUN mvn -B clean package -DskipTests


# ================================
# 2. Run Stage
# ================================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy compiled jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render provides PORT env variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
