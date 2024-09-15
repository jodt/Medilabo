FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} patient-ms.jar

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "/patient-ms.jar"]