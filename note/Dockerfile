FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} note-ms.jar

EXPOSE 9002

ENTRYPOINT ["java", "-jar", "/note-ms.jar"]