package ru.kuzmin.userqueryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class UserQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserQueryServiceApplication.class, args);
    }
}
