FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/post-service-1.1.jar PostService.jar
ENTRYPOINT ["java", "-jar", "PostService.jar"]