package com.bobocode.demo.service;


import com.bobocode.demo.annotation.Bean;

@Bean(name = "cya")
public class ByeService {

    public void sayCya() {
        System.out.println("Cya");
    }
}
