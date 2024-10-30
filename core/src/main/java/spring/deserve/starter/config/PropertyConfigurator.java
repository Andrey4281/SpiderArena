package spring.deserve.starter.config;

import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;
import spring.deserve.starter.annotainon.InjectProperty;

@Component
public class PropertyConfigurator implements BeanPostProcessor {

   @Autowired
   private Environment environment;



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        configure(bean);
        return bean;
    }

    public void configure(Object object) {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @spring.deserve.it.annotainon.InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(InjectProperty.class));

        allFields.forEach(field -> {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            String propertyValue = environment.getProperty(annotation.value());
            if (propertyValue != null) {
                field.setAccessible(true);
                try {
                    Object valueToInject = convertValue(field.getType(), propertyValue);
                    field.set(object, valueToInject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Object convertValue(Class<?> targetType, String value) {
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        // Поддержка других типов по мере необходимости
        return value;
    }
}
