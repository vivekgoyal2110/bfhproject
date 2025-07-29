package com.bajaj.bfhproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BfhprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BfhprojectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppService appService) {
        return args -> appService.start();
    }
}
