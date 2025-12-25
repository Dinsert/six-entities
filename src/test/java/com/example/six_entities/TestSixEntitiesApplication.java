package com.example.six_entities;

import com.example.six_entities.config.TestContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestSixEntitiesApplication {

    public static void main(String[] args) {
        SpringApplication.from(SixEntitiesApplication::main).with(TestContainersConfiguration.class).run(args);
    }

}
