package com.bobocode.demo;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoWebApp {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(DemoWebApp.class, args);
    }
}