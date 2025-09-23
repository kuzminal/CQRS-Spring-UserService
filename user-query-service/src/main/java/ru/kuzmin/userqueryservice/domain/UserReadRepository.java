package ru.kuzmin.userqueryservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserReadRepository extends MongoRepository<UserReadModel, String> {
}