# Use Eclipse Temurin JDK 17 (recommended for Spring Boot)
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/noteproject-0.0.1-SNAPSHOT.jar"]
