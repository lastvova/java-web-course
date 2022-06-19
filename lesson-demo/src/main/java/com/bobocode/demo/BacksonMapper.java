package com.bobocode.demo;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BacksonMapper {
    @SneakyThrows
    public static <T> T readObject(String json, Class<T> clazz) {
        json = json.replace("{", "")
                .replace("}", "")
                .replace("\n", "")
                .replace("\"", "");

        String[] split = json.split(",");

        Map<String, String> collect = Arrays.stream(split)
                .map(String::trim)
                .map(e -> Map.entry(e.split(":")[0].trim(),
                        e.split(":")[1].trim()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Constructor<?> constructors = clazz.getConstructor();
        T instance = clazz.cast(constructors.newInstance());
        Arrays.stream(clazz.getDeclaredFields())
                .filter(e -> collect.containsKey(e.getName()))
                .forEach(e -> {
                    e.setAccessible(true);
                    try {
                        e.set(instance, collect.get(e.getName()));
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                });

        return instance;
    }
}
