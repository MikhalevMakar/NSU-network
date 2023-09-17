FROM gradle:8.3-jdk19 AS build

COPY . .

RUN gradle build --extensions='quartz'

FROM openjdk:19-slim

WORKDIR /app

COPY ./build/libs/Task1-1.0-SNAPSHOT.jar ./app.jar
COPY quartz-2.3.2.jar /app/

CMD ["java", "-jar", "app.jar"]