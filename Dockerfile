FROM openjdk:21-slim

WORKDIR /app

# Copy Maven build files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Copy source
COPY src src

# Build the application
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests && \
    rm -rf .mvn mvnw pom.xml src

# Copy the built jar
RUN cp target/SplitSense-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

