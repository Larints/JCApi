# Используем базовый образ с Java 17 и Maven для сборки
FROM maven:3.8.4-openjdk-17 AS build

# Копируем исходный код приложения в контейнер
COPY ./ /app
WORKDIR /app

# Собираем приложение с помощью Maven
RUN mvn clean package -DskipTests

# Второй этап: создаем образ приложения с Java Runtime 17
FROM openjdk:17-jdk-slim

# Копируем собранный JAR файл из предыдущего этапа в контейнер
COPY --from=build /app/target/*.jar /app/app.jar

# Устанавливаем точку входа для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
