# Use a imagem base do Maven para construir a aplicação
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Use uma imagem base do OpenJDK para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/loading-ps2024-0.0.1-SNAPSHOT.jar my-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar", "my-app.jar"]
