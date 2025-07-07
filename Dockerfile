# Step 1: Use a base image with Java installed
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file into the container
COPY target/e-commerce-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
