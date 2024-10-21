package spring.deserve.it.common;

import org.reflections.ReflectionUtils;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.PaperSpider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class InjectPropertyObjectConfigurator {
    public void configure(Spider spider) throws Exception {
        processInjectProperty(spider);
    }

    private void processInjectProperty(Spider spider) throws Exception {
        Set<Field> allFields = ReflectionUtils.getAllFields(spider.getClass(), field -> field.isAnnotationPresent(InjectProperty.class));
        for (Field field : allFields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if(!(annotation instanceof InjectProperty)){
                    continue;
                }

                String propertyName = ((InjectProperty) annotation).propertyName();
                List<String> propertiesContent = Files.readAllLines(Path.of("src/main/resources/application.properties"));
                for (String propertyLine : propertiesContent) {
                    String[] keyValue = propertyLine.split("=");
                    if(keyValue[0].equals(propertyName)){
                        field.setAccessible(true);
                        field.set(spider, Integer.parseInt(keyValue[1]));
                    }
                }

            }
        }
    }
}
