FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/user-service-1.1.jar UserService.jar
ENTRYPOINT ["java", "-jar", "UserService.jar"]