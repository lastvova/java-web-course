package com.bobocode.demo;

import lombok.SneakyThrows;

public class DemoWebApp {
    @SneakyThrows
    public static void main(String[] args) {
        String s = "{\n" +
                "  \"firstName\": \"Andrii\",\n" +
                "  \"lastName\": \"Shtramak\",\n" +
                "  \"city\": \"Zarichchya\"\n" +
                "}";
        System.out.println(BacksonMapper.readObject(s, User.class));
    }
}
