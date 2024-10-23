package spring.deserve.it.infra;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import spring.deserve.it.common.ObjectFactory;
import spring.deserve.it.common.Singleton;


public class ApplicationContext {

    Map<Class<?>, Object> beans = new HashMap<>();
    ObjectFactory objectFactory = ObjectFactory.getInstance();

    public ApplicationContext(String packageToScan) {
        objectFactory.postConstruct(this, packageToScan);
    }

    public Object getObject(Class<?> clazz) {
        boolean isSingleton = Arrays.stream(clazz.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation instanceof Singleton);
        if (isSingleton) {
            beans.putIfAbsent(clazz, objectFactory.createObject(clazz));
            return beans.get(clazz);
        }
        return objectFactory.createObject(clazz);
    }
}
