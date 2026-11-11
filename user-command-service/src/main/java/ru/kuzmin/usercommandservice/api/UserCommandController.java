package ru.kuzmin.usercommandservice.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.kuzmin.usercommandservice.command.CreateUserCommand;
import ru.kuzmin.usercommandservice.command.UserCommandService;

@RestController
@RequestMapping("/api/users")
public class UserCommandController {

    private final UserCommandService userCommandService;

    public UserCommandController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequest request) {
        return userCommandService.createUser(
                new CreateUserCommand(request.getUsername(), request.getEmail()));
    }
}
