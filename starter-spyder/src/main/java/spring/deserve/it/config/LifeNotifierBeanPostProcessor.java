package spring.deserve.it.config;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import spring.deserve.starter.spider.Spider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Component
public class LifeNotifierBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ConfigurableListableBeanFactory factory;

    @Autowired
    private Environment environment;

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
        if (beanDefinition.getBeanClassName() != null) {
            Class<?> originalClass = Class.forName(beanDefinition.getBeanClassName());
            return wrapWithProxy(bean, originalClass);
        }

        return bean;
    }

    @SneakyThrows
    public <T> T wrapWithProxy(T obj, Class implClass) {
        //TODO
        // Проверяем, есть ли методы с аннотацией @spring.deserve.it.annotainon.Log в реализации класса
        if (Arrays.stream(implClass.getMethods()).anyMatch(m -> m.getName().equals("loseLife"))
                && environment.getProperty("remaining.life") != null) {
            // Создаем прокси-объект
            return (T) Proxy.newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(), new InvocationHandler() {
                @Override
                @SneakyThrows
                public Object invoke(Object proxy, Method method, Object[] args) {
                    // Получаем оригинальный метод из реализации класса
                    Method originalMethod = implClass.getMethod(method.getName(), method.getParameterTypes());
                    Object result = originalMethod.invoke(obj, args);  // Вызов реального метода
                    Spider spider = (Spider) obj;
                    if (spider.getLives() == Integer.parseInt(environment.getProperty("remaining.life"))) {
                        System.out.println("%s у паука %s осталась %d жизней".formatted(spider.getOwner(), spider.hashCode(), spider.getLives()));
                    }
                    return result;
                }
            });
        }
        return obj;
    }
}
