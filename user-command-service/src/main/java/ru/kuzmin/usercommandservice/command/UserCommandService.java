package ru.kuzmin.usercommandservice.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kuzmin.usercommandservice.domain.User;
import ru.kuzmin.usercommandservice.domain.UserRepository;
import ru.kuzmin.usercommandservice.event.UserCreatedEvent;
import ru.kuzmin.usercommandservice.outbox.OutboxEvent;
import ru.kuzmin.usercommandservice.outbox.OutboxEventRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserCommandService {

    private final UserRepository userRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public UserCommandService(UserRepository userRepository,
                              OutboxEventRepository outboxEventRepository,
                              ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Создание пользователя: состояние и событие сохраняются в одной транзакции
     * (transactional outbox), публикация в Kafka — асинхронно через relay.
     */
    @Transactional
    public String createUser(CreateUserCommand command) {
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setId(userId);
        user.setUsername(command.username());
        user.setEmail(command.email());
        userRepository.save(user);

        saveToOutbox(new UserCreatedEvent(userId, user.getUsername(), user.getEmail()));

        return userId;
    }

    private void saveToOutbox(UserCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            outboxEventRepository.save(new OutboxEvent(
                    null,
                    UserCreatedEvent.class.getSimpleName(),
                    event.getId(),
                    payload,
                    Instant.now(),
                    false,
                    null));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize event", e);
        }
    }
}
