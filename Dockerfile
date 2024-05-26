FROM eclipse-temurin:21-jdk-alpine
ADD target/loading-ps2024-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]