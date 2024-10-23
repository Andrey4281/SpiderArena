package spring.deserve.it.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.ReflectionUtils;
import static org.reflections.ReflectionUtils.withAnnotation;
import spring.deserve.it.infra.ApplicationContext;

public class InjectObjectConfigurator implements ObjectConfigurator {

    private ApplicationContext context;

    @Override
    public void configure(Object object) throws Exception {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(Inject.class));

        for (Field field : allFields) {
            Object value = context.getObject(field.getType());
            field.setAccessible(true);
            field.set(object, value);
        }

        Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz, withAnnotation(Inject.class));

        for (Method method : allMethods) {
            if (method.getName().startsWith("set")
                    && method.getParameterCount() == 1) {
                Class<?> methodType = method.getParameterTypes()[0];
                Object value = context.getObject(methodType);
                method.setAccessible(true);
                method.invoke(object, value);
            }
        }
    }

    @Override
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

}
