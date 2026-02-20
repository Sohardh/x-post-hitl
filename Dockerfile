# Use eclipse-temurin for Java 25 as the base image for both build and run stages
FROM eclipse-temurin:25-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre

# Set the working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/x-post-hitl-0.0.1-SNAPSHOT.jar app.jar

# Set the timezone
ENV TZ=Asia/Calcutta

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
