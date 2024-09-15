FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} client-ms.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/client-ms.jar"]