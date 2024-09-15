FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} risk-ms.jar

EXPOSE 9003

ENTRYPOINT ["java", "-jar", "/risk-ms.jar"]