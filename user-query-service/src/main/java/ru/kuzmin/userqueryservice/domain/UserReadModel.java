package ru.kuzmin.userqueryservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class UserReadModel {
    @Id
    private String id;
    private String username;
    private String email;
}