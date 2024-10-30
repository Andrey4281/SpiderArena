package spring.deserve.starter.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.PlayerQualifier;
import spring.deserve.starter.spider.Spider;

@Component
public class OwnerSetterBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof Spider spider){
            String playerName = bean.getClass().getAnnotation(PlayerQualifier.class).value();
            spider.setOwner(playerName);
        }

        return bean;
    }
}
