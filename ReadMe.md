# CQRS Microservices Project

Этот проект демонстрирует реализацию паттерна CQRS (Command Query Responsibility Segregation) с использованием двух отдельных микросервисов для операций записи и чтения.

## 📋 Оглавление

- [Архитектура](#архитектура)
- [Технологии](#технологии)
- [Структура проекта](#структура-проекта)
- [Установка и запуск](#установка-и-запуск)
- [API Endpoints](#api-endpoints)
- [Примеры использования](#примеры-использования)
- [Разработка](#разработка)

## 🏗️ Архитектура

Проект состоит из двух независимых микросервисов:

### **User Command Service** (`user-command-service`)
- **Порт**: 8081
- **Назначение**: Обработка операций записи (CREATE, UPDATE, DELETE)
- **База данных**: PostgreSQL (транзакционные данные)
- **Коммуникация**: Публикация событий в Kafka

### **User Query Service** (`user-query-service`)
- **Порт**: 8082
- **Назначение**: Обработка операций чтения (GET, SEARCH)
- **База данных**: MongoDB (оптимизирована для запросов)
- **Коммуникация**: Подписка на события из Kafka

### Поток данных:
```
Client → Command Service → PostgreSQL → Kafka → Query Service → MongoDB → Client
```

## 🛠️ Технологии

- **Java 21**
- **Spring Boot 3.x**
- **Apache Kafka** - обмен событиями между сервисами
- **PostgreSQL** - база данных для команд (запись)
- **MongoDB** - база данных для запросов (чтение)
- **Maven** - управление зависимостями
- **Docker** - контейнеризация зависимостей

## 📁 Структура проекта

```
cqrs-microservices/
├── user-command-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/ru/kuzmin/usercommandservice/
│   │       │   ├── api/              # REST контроллеры
│   │       │   ├── command/          # Команды и обработчики
│   │       │   ├── domain/           # Сущности и репозитории
│   │       │   ├── event/            # События
│   │       │   └── config/           # Конфигурации
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── user-query-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/ru/kuzmin/userqueryservice/
│   │       │   ├── api/              # REST контроллеры
│   │       │   ├── query/            # Запросы и обработчики
│   │       │   ├── domain/           # Модели для чтения
│   │       │   ├── event/            # Обработчики событий
│   │       │   └── config/           # Конфигурации
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
└── docker-compose.yml
```

## 🚀 Установка и запуск

### Запуск инфраструктуры

```bash
# Запуск PostgreSQL, MongoDB и Kafka
docker-compose up -d
```

Проверьте, что все контейнеры запущены:
```bash
docker-compose ps
```

### Проверка работоспособности

После запуска сервисы будут доступны:
- Command Service: http://localhost:8081
- Query Service: http://localhost:8082

## 📡 API Endpoints

### Command Service (порт 8081)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | `/api/users` | Создание нового пользователя |

**Пример запроса:**
```bash
    curl -X POST http://localhost:8081/api/users \
      -H "Content-Type: application/json" \
      -d '{
        "username": "john_doe",
        "email": "john@example.com"
      }'
```

**Пример ответа:**
```json
"a1b2c3d4-e5f6-7890-abcd-ef1234567890"
```

### Query Service (порт 8082)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `/api/users/{id}` | Получение пользователя по ID |

**Пример запроса:**
```bash
  curl http://localhost:8082/api/users/a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

**Пример ответа:**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "username": "john_doe",
  "email": "john@example.com"
}
```

## 💡 Примеры использования

### Полный цикл создания и чтения пользователя

```bash
    # 1. Создание пользователя через Command Service
    USER_ID=$(curl -s -X POST http://localhost:8081/api/users \
      -H "Content-Type: application/json" \
      -d '{"username": "alice", "email": "alice@example.com"}' | tr -d '"')
    
    echo "Created user with ID: $USER_ID"
    
    # 2. Небольшая задержка для обработки события (eventual consistency)
    sleep 2
    
    # 3. Чтение пользователя через Query Service
    curl http://localhost:8082/api/users/$USER_ID
```

### Проверка событий в Kafka

```bash
# Просмотр топиков Kafka
    docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092
    
    # Просмотр сообщений в топике user-events
    docker exec -it kafka kafka-console-consumer.sh \
      --topic user-events \
      --from-beginning \
      --bootstrap-server localhost:9092
```

## 🔧 Разработка

### Добавление новых команд

1. **В Command Service:**
```java
// 1. Создайте команду
public class UpdateUserCommand {
    private String id;
    private String username;
    // getters/setters
}

// 2. Добавьте метод в сервис
public String updateUser(UpdateUserCommand command) {
    // бизнес-логика
    // публикация события UserUpdatedEvent
}

// 3. Добавьте endpoint в контроллер
@PutMapping("/{id}")
public String updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
    // вызов сервиса
}
```

2. **В Query Service:**
```java
// 1. Обработайте событие
@KafkaListener(topics = "user-events")
public void handleUserUpdatedEvent(UserUpdatedEvent event) {
    // обновление данных в MongoDB
}
```

### Проверка соединений

```bash
    # Проверка PostgreSQL
    psql -h localhost -p 5432 -U postgres -d user_command_db
    
    # Проверка MongoDB
    mongosh mongodb://localhost:27017/user_query_db
    
    # Проверка Kafka
    docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092
```
