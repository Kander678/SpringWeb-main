# Stage 1: Build the application
FROM gradle:7.6-jdk17 AS build

WORKDIR /app

# Copy Gradle wrapper and configuration files
COPY gradlew gradlew
COPY gradle/ gradle/
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle

# Make gradlew executable
RUN chmod +x gradlew

# Cache Gradle dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src/ src/

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Create a minimal runtime image
FROM eclipse-temurin:17-jre-jammy AS runtime

# Create a non-root user
RUN adduser --disabled-password --gecos "" --home "/nonexistent" --shell "/sbin/nologin" appuser
USER appuser

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
