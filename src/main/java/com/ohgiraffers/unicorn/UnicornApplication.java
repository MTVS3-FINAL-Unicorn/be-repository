package com.ohgiraffers.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UnicornApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.ohgiraffers.bridge.UnicornApplication.class, args);
    }

}
