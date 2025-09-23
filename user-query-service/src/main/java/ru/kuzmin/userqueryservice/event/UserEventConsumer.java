package ru.kuzmin.userqueryservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kuzmin.userqueryservice.domain.UserReadModel;
import ru.kuzmin.userqueryservice.domain.UserReadRepository;

@Service
public class UserEventConsumer {
    private final UserReadRepository userReadRepository;

    public UserEventConsumer(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @KafkaListener(topics = "user-events", groupId = "user-query-group")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        UserReadModel user = new UserReadModel();
        user.setId(event.getId());
        user.setUsername(event.getUsername());
        user.setEmail(event.getEmail());

        userReadRepository.save(user);
    }
}