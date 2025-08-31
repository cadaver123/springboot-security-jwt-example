FROM openjdk:23-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file (adjust filename)
COPY build/libs/springboot-security-jwt-example-1.0-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Define environment variables
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV ADMIN_API_KEY=${ADMIN_API_KEY}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
