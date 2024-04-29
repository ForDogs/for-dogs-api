package com.fordogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ForDogsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForDogsApiApplication.class, args);
    }

}
