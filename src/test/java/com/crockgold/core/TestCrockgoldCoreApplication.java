package com.crockgold.core;

import org.springframework.boot.SpringApplication;

public class TestCrockgoldCoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(CrockgoldCoreApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
