package ru.kuzmin.userqueryservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import ru.kuzmin.userqueryservice.domain.UserReadModel;
import ru.kuzmin.userqueryservice.domain.UserReadRepository;

@Service
public class UserEventConsumer {

    private final UserReadRepository userReadRepository;
    private final ObjectMapper objectMapper;

    public UserEventConsumer(UserReadRepository userReadRepository, ObjectMapper objectMapper) {
        this.userReadRepository = userReadRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-events", groupId = "user-query-group")
    public void handleUserCreatedEvent(String payload) {
        try {
            UserCreatedEvent event = objectMapper.readValue(payload, UserCreatedEvent.class);

            UserReadModel user = new UserReadModel();
            user.setId(event.getId());
            user.setUsername(event.getUsername());
            user.setEmail(event.getEmail());

            userReadRepository.save(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize event", e);
        }
    }
}
