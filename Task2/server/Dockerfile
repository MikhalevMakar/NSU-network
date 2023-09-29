FROM gradle:8.2-jdk19 AS build

FROM openjdk:19-slim

WORKDIR /app

ADD ./build/libs/server-app-1.0-SNAPSHOT-all.jar ./server-app.jar

CMD ["java", "-jar", "server-app.jar", "1234"]