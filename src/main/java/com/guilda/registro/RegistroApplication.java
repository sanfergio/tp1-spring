package com.guilda.registro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RegistroApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistroApplication.class, args);
    }
}