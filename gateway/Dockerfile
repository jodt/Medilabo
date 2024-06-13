FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gateway.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/gateway.jar"]