FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y maven openjdk-21-jdk
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]