package ru.kuzmin.usercommandservice.api;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
}
