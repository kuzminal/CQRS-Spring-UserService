package ru.kuzmin.userqueryservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuzmin.userqueryservice.query.UserQueryService;
import ru.kuzmin.userqueryservice.query.UserResponse;

@RestController
@RequestMapping("/api/users")
public class UserQueryController {
    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id) {
        return userQueryService.getUser(id);
    }
}
