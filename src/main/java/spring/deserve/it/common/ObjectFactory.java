package spring.deserve.it.common;

import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class ObjectFactory {

    private static ObjectFactory objectFactory;
    private List<? extends ObjectConfigurator> configurators;

    private ObjectFactory() {
        Reflections reflections = new Reflections("spring.deserve.it.common");
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);
        configurators = configuratorClasses.stream()
                .map(this::newInstance)
                .toList();
    }

    @SneakyThrows
    private ObjectConfigurator newInstance(Class<? extends ObjectConfigurator> configurator) {
            return configurator.getDeclaredConstructor().newInstance();
    }


    @SneakyThrows
    public <T>T createObject(Class<T> clazz) {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(instance);
        }
        return instance;
    }

    static public ObjectFactory getInstance() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }




}
