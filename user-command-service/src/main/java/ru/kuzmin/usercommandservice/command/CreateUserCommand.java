package ru.kuzmin.usercommandservice.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserCommand {
    private String id;
    private String username;
    private String email;
}
