package spring.deserve.it.common;

import java.io.InputStream;
import java.util.Properties;
import org.reflections.ReflectionUtils;
import static org.reflections.ReflectionUtils.withAnnotation;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.PaperSpider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class InjectPropertyObjectConfigurator implements ObjectConfigurator {
    private final Properties properties = new Properties();


    public InjectPropertyObjectConfigurator() {
        // Загружаем application.properties из ресурсов
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Файл application.properties не найден в ресурсах");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configure(Object object) {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(InjectProperty.class));

        allFields.forEach(field -> {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            String propertyValue = properties.getProperty(annotation.value());
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
