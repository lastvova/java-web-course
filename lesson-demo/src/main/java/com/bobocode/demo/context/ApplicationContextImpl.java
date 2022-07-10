package com.bobocode.demo.context;

import com.bobocode.demo.annotation.Bean;
import com.bobocode.demo.exception.NoSuchBeanException;
import com.bobocode.demo.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApplicationContextImpl implements ApplicationContext {

    private Map<String, Object> beanStorage;

    public ApplicationContextImpl(String packageName) {
        beanStorage = new HashMap<>();
        init(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        Objects.requireNonNull(beanType);
        List<T> beans = beanStorage.values().stream()
                .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
                .map(beanType::cast)
                .toList();
        if (beans.isEmpty()) {
            throw new NoSuchBeanException("No such bean for type " + beanType.getSimpleName());
        }
        if (beans.size() > 1) {
            throw new NoUniqueBeanException("No unique beans for type " + beanType.getSimpleName());
        }
        return beans.get(0);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(beanType);

        Object bean = beanStorage.get(name);
        if (!beanType.isInstance(bean)) {
            throw new NoSuchBeanException(String.format("No such bean with name: %s and type %s",
                    name, beanType.getSimpleName()));
        }
        return beanType.cast(bean);
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beanStorage.entrySet().stream()
                .filter(bean -> beanType.isAssignableFrom(bean.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, bean -> beanType.cast(bean.getValue())));
    }

    private void init(String packageName) {
        Reflections reflections = new Reflections(packageName);
        var annotatedClasses = reflections.getTypesAnnotatedWith(Bean.class);

        for (Class<?> type : annotatedClasses) {
            try {
                var instance = type.getConstructor().newInstance();
                var beanName = resolveBeanName(type);
                this.beanStorage.put(beanName, instance);
            } catch (InstantiationException | NoSuchMethodException
                    | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String resolveBeanName(Class<?> type) {
        String beanName = type.getAnnotation(Bean.class).name();
        return beanName.isBlank() ? getBeanNameFromType(type) : beanName;

    }

    private String getBeanNameFromType(Class<?> type) {
        String simpleName = type.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
