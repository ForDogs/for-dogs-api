package com.fordogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ForDogsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForDogsApiApplication.class, args);
    }
}
