# Техническое задание проекта

Написать REST приложение вида пополнения кошелька, снятие средств с кошелька, а так же получения кошелька по UUID

### Технологический стек
- Java: версии 8-17 
- Spring Framework: версия 3 
- PostgreSQL 
- Liquibase для управления миграциями базы данных 
- Docker и Docker Compose для контейнеризации приложения и базы данных

### Настройка и запуск

Для запуска приложения, должен быть установлен DOCKER
в корне проекта, поднимаем образ командой docker-compose up

_docker-compose --env-file build.env up --build_

Используя предустановки для переменных сред.
