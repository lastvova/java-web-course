package com.bobocode.demo.service;

import com.bobocode.demo.annotation.Bean;

@Bean
public class HelloService implements GreetingService {

    @Override
    public void saySomething(String name) {
        System.out.println("Hello " + name);
    }
}
