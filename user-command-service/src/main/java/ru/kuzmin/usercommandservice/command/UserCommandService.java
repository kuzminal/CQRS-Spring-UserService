package ru.kuzmin.usercommandservice.command;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.kuzmin.usercommandservice.domain.User;
import ru.kuzmin.usercommandservice.domain.UserRepository;
import ru.kuzmin.usercommandservice.event.UserCreatedEvent;

@Service
public class UserCommandService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    
    public UserCommandService(UserRepository userRepository, 
                           KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Transactional
    public String createUser(CreateUserCommand command) {
        User user = new User();
        user.setId(command.getId());
        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        
        userRepository.save(user);
        
        // Публикуем событие
        kafkaTemplate.send("user-events", new UserCreatedEvent(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        ));
        
        return user.getId();
    }
}
