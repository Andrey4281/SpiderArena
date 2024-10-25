package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import static org.reflections.ReflectionUtils.withAnnotation;
import org.reflections.Reflections;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class ObjectFactory {

    // Контекст, с которым работает фабрика
    private final ApplicationContext context;

    // Набор конфигураторов
    private final Set<ObjectConfigurator> configurators;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;

        // Получаем Reflections из контекста и сканируем пакет для поиска ObjectConfigurator
        Reflections reflections = context.getReflections();
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);

        configurators = configuratorClasses.stream()
                .map(this::createInstance)
                .peek(configurator -> configurator.setApplicationContext(context))
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public <T> T createObject(Class<T> clazz) {
        T obj = clazz.getDeclaredConstructor().newInstance();

        // Применяем все конфигураторы, передавая им контекст
        for (ObjectConfigurator configurator : configurators) {
            // Передаем контекст через сеттер
            configurator.configure(obj);
        }
        invokePostConstruct(obj);
        if (ReflectionUtils.getAllMethods(obj.getClass(), withAnnotation(Log.class)).size() > 0) {
            obj = (T) createProxyObject(obj);
        }

        return obj;
    }

    @SneakyThrows
    private <T> Object createProxyObject(T object) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (Arrays.stream(method.getAnnotations()).anyMatch(annotation -> annotation instanceof Log)) {
                Log annotation = method.getAnnotation(Log.class);
                String propertyToLog = annotation.value();
                Object valueToLog = getValueToLog(obj, propertyToLog);
                System.out.println(String.format("Logged %s : %s = %s", obj.getClass(), propertyToLog, valueToLog));
            }
            return proxy.invokeSuper(obj, args);
        });
        T proxy = (T) enhancer.create();
        return proxy;
    }

    private Object getValueToLog(Object obj, String propertyToLog) throws IllegalAccessException {
        Field field = getField(obj, propertyToLog);
        field.setAccessible(true);
        return field.get(obj);
    }

    private Field getField(Object obj, String propertyToLog) {
        return ReflectionUtils.getAllFields(obj.getClass())
                .stream()
                .filter(f -> f.getName().equals(propertyToLog))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @SneakyThrows
    private ObjectConfigurator createInstance(Class<? extends ObjectConfigurator> clazz) {
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
