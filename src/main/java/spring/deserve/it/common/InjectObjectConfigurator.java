package spring.deserve.it.common;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

public class InjectObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object object) throws Exception {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(Inject.class));

        for (Field field : allFields) {
            Object value = ObjectFactory.getInstance().createObject(field.getType());
            field.setAccessible(true);
            field.set(object, value);
        }
    }
}