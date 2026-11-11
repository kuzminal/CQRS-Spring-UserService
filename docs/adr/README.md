# Architecture Decision Records

Индекс архитектурных решений проекта **CQRS — User Service**.

| № | Название | Статус |
|---|----------|--------|
| [0001](0001-cqrs-command-query-separation.md) | CQRS: разделение команд и запросов | ✅ Accepted |
| [0002](0002-event-driven-sync-kafka.md) | Синхронизация read-модели через Kafka | ✅ Accepted |
| [0003](0003-polyglot-persistence-postgres-mongo.md) | Разные хранилища: PostgreSQL + MongoDB | ✅ Accepted |
| [0004](0004-event-contract-duplication.md) | Дублирование контракта события между сервисами | ✅ Accepted |
| [0005](0005-transactional-outbox.md) | Transactional Outbox для надёжной публикации событий | 🔶 Proposed |

## Формат

Каждый ADR описывает одно решение в формате:

- **Контекст** — какая проблема решается и какие альтернативы рассматривались;
- **Решение** — что выбрано и почему;
- **Последствия** — плюсы и минусы принятого решения.

Статусы: `Proposed` → `Accepted` → `Deprecated` / `Superseded`.
