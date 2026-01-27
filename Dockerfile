# Stage 1: Build (Optional, or just use the pre-built jar)
FROM openjdk:23-jdk-slim
WORKDIR /app

# Copy the jar file from the target folder
COPY target/JavaSprint5_1BlackJack-0.0.1-SNAPSHOT.jar app.jar

# Define environmental variables for the database
ENV MYSQL_URL=r2dbc:mysql://mysql-container:3306/blackjack
ENV MONGO_URI=mongodb://mongo-container:27017/blackjack

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]