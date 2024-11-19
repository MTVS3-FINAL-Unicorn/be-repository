package com.ohgiraffers.unicorn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableAsync
public class UnicornApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.ohgiraffers.unicorn.UnicornApplication.class, args);
    }

}
