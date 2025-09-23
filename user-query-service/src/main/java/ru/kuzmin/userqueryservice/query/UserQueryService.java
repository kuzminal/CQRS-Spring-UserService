package ru.kuzmin.userqueryservice.query;

import org.springframework.stereotype.Service;
import ru.kuzmin.userqueryservice.domain.UserReadRepository;

@Service
public class UserQueryService {
    private final UserReadRepository userReadRepository;

    public UserQueryService(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    public UserResponse getUser(String id) {
        return userReadRepository.findById(id)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}