FROM openjdk:17-jdk

WORKDIR /app

COPY doomz-0.0.1-SNAPSHOT.jar app.jar

COPY .env .env

ENTRYPOINT ["java", "-jar", "app.jar"]
