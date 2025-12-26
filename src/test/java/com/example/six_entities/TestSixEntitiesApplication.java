package com.example.six_entities;

import org.springframework.boot.SpringApplication;

public class TestSixEntitiesApplication {

    public static void main(String[] args) {
        SpringApplication.from(SixEntitiesApplication::main).with(TestContainersConfiguration.class).run(args);
    }

}
