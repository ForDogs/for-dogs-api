# Build stage
FROM bellsoft/liberica-openjdk-alpine:17 as build
WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' ./gradlew
RUN ./gradlew bootJar

# Runtime stage
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose HTTPS port
EXPOSE 443

# Set the command to run the application with custom configuration
ENTRYPOINT ["java", "-Xmx1g", "-Xms1g", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/dumps", "-jar", "-Dspring.profiles.active=local", "/app/app.jar"]
