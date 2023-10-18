FROM gradle:8.2-jdk19 AS build
FROM openjdk:19-slim

WORKDIR /app

ADD ./build/libs/client-1.0-SNAPSHOT-all.jar ./client-app.jar

CMD ["java", "-jar", "client-app.jar", "-h", "172.16.0.3", "-f", "/app/src/main/resources/data.txt", "-p", "1234"]