package spring.deserve.it.infra;

import jakarta.annotation.PreDestroy;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import spring.deserve.it.game.Inject;

import java.util.Arrays;
import java.util.Objects;

import static org.reflections.ReflectionUtils.withAnnotation;

@Component
public class BeanDefinitionValidatorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        boolean hasPreDestroyAnnotationOnPrototypeBean = Arrays.stream(beanFactory.getBeanDefinitionNames())
                .filter(bd -> Objects.equals(beanFactory.getBeanDefinition(bd).getScope(), ConfigurableListableBeanFactory.SCOPE_PROTOTYPE))
                .anyMatch(prototypeBeanDefinitionName -> {
                    BeanDefinition beanDefinition = beanFactory.getBeanDefinition(prototypeBeanDefinitionName);
                    try {
                        Class<?> originalClass = Class.forName(beanDefinition.getBeanClassName());
                        return ReflectionUtils.getAllMethods(originalClass).stream()
                                .anyMatch(m -> m.isAnnotationPresent(PreDestroy.class));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
        if (hasPreDestroyAnnotationOnPrototypeBean) {
            throw new RuntimeException("Error! You have PreDestroy annotation on prototype bean");
        }
    }
}
