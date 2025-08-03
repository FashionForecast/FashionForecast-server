FROM eclipse-temurin:17-jdk
ENV TZ=Asia/Seoul
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
