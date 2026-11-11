package ru.kuzmin.usercommandservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserCommandServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCommandServiceApplication.class, args);
    }
}
