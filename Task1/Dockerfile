# Используйте базовый образ с Java
#FROM openjdk:17-jdk-slim
#
## Устанавливаем рабочую директорию внутри контейнера
#WORKDIR .
#
## Копируем JAR-файл вашего приложения в контейнер
#COPY gradle/wrapper/gradle-wrapper.jar .
#EXPOSE 8080
#
## Указываем точку входа для выполнения вашего приложения
#CMD ["java", "-jar", "gradle-wrapper.jar"]

# Используйте базовый образ с Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем Gradle Wrapper и build.gradle для сборки
COPY gradle/wrapper /app/gradle/wrapper/
COPY build.gradle /app/

# Копируем исходный код приложения
COPY src /app/src

# Собираем приложение с помощью Gradle Wrapper
RUN ./gradle/wrapper/gradle-wrapper.jar build

# Указываем точку входа для выполнения вашего приложения
CMD ["java", "-jar", "build/libs/gradle-wrapper.jar"]