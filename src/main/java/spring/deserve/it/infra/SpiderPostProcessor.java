package spring.deserve.it.infra;

import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.Spider;
import static org.reflections.ReflectionUtils.withAnnotation;
import spring.deserve.it.game.PlayerQualifier;


@Component
public class SpiderPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Spider) {
            PlayerQualifier annotation = bean.getClass().getAnnotation(PlayerQualifier.class);
            if (annotation == null) {
                throw new IllegalStateException("Spider without PlayerQualifier");
            }
            ((Spider) bean).setOwner(annotation.value());
        }
        return bean;
    }

//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
}
