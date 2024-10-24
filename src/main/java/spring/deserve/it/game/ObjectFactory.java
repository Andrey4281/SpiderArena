package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.reflections.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import static java.lang.reflect.Proxy.*;
import static org.reflections.ReflectionUtils.withAnnotation;

public class ObjectFactory {

    // Контекст, с которым работает фабрика
    private final ApplicationContext context;

    // Набор конфигураторов
    private final Set<ObjectConfigurator> configurators;

    private final Set<ProxyConfigurator> proxyConfigurators;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;

        // Получаем Reflections из контекста и сканируем пакет для поиска ObjectConfigurator
        Reflections reflections = context.getReflections();
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);

        configurators = configuratorClasses.stream()
                .map(this::createObjectConfiguratorInstance)
                .peek(configurator -> configurator.setApplicationContext(context))
                .collect(Collectors.toSet());

        Set<Class<? extends ProxyConfigurator>> proxyConfiguratorClasses = reflections.getSubTypesOf(ProxyConfigurator.class);

        proxyConfigurators = proxyConfiguratorClasses.stream()
                .map(this::createProxyConfiguratorInstance)
                .peek(configurator -> configurator.setApplicationContext(context))
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public <T> T createObject(Class<T> originalClazz) {
        Class<? extends T> implClass = getImplClass(originalClazz);
        T obj = implClass.getDeclaredConstructor().newInstance();

        // Применяем все конфигураторы, передавая им контекст
        for (ObjectConfigurator configurator : configurators) {
          // Передаем контекст через сеттер
            configurator.configure(obj);
        }
        invokePostConstruct(obj);

        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            obj = (T) proxyConfigurator.configureProxy(obj, implClass);
        }


        return obj;
    }

    private <T> Class<? extends T> getImplClass(Class<T> originalClass) {
        if (originalClass.isInterface()) {
            Set<Class<? extends T>> subTypesOf = context.getReflections().getSubTypesOf(originalClass);
            if (subTypesOf.size() != 1) {
                throw new RuntimeException("You have more than one implementation");
            }
            return subTypesOf.iterator().next();
        } else {
            return originalClass;
        }
    }

    @SneakyThrows
    private ObjectConfigurator createObjectConfiguratorInstance(Class<? extends ObjectConfigurator> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private ProxyConfigurator createProxyConfiguratorInstance(Class<? extends ProxyConfigurator> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private <T> void invokePostConstruct(T obj) {
        // Ищем все методы, помеченные @PostConstruct
        Set<Method> postConstructMethods = ReflectionUtils.getAllMethods(obj.getClass(), withAnnotation(PostConstruct.class));

        // Вызываем каждый метод
        for (Method method : postConstructMethods) {
            method.setAccessible(true);
            method.invoke(obj);  // SneakyThrows обрабатывает исключения
        }
    }

    // Метод для получения контекста
    public ApplicationContext getContext() {
        return context;
    }
}
