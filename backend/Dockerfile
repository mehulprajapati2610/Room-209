# Multi-stage build for Spring Boot backend
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Firebase credentials (if provided via environment variable, write to file)
RUN echo "Firebase config will be injected at runtime"

EXPOSE 8080
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "app.jar"]
