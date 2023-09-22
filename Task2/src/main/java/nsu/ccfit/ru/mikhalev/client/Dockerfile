FROM gradle:8.3-jdk19 AS build

COPY . .

RUN gradle build

FROM openjdk:19-slim

WORKDIR /app

ADD ./build/libs/Task1-1.0-SNAPSHOT-all.jar ./app.jar

ENTRYPOINT java -jar app.jar