package com.bobocode.demo.service;

import com.bobocode.demo.annotation.Bean;

@Bean(name = "someBean")
public class WhatUpService implements GreetingService {

    @Override
    public void saySomething(String name) {
        System.out.println("What`s up " + name);
    }
}
