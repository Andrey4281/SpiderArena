package spring.deserve.starter.config;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.Benchmark;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Component
public class BenchmarkBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ConfigurableListableBeanFactory factory;
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
        // Проверяем, есть ли методы с аннотацией @spring.deserve.it.annotainon.Log в реализации класса
        if (Arrays.stream(implClass.getMethods()).anyMatch(this::hasBenchMarkAnnotation)) {
            // Создаем прокси-объект
            return (T) Proxy.newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(), new InvocationHandler() {
                @Override
                @SneakyThrows
                public Object invoke(Object proxy, Method method, Object[] args) {
                    // Получаем оригинальный метод из реализации класса
                    Method originalMethod = implClass.getMethod(method.getName(), method.getParameterTypes());

                    // Проверяем, есть ли у оригинального метода аннотация @spring.deserve.it.annotainon.Log
                    if (originalMethod.isAnnotationPresent(Benchmark.class)) {
                        long startTime = System.currentTimeMillis();
                        Object result = originalMethod.invoke(obj, args);  // Вызов реального метода
                        long endTime = System.currentTimeMillis();
                        System.out.println("Метод %s отработал за время %d".formatted(originalMethod.getName(), endTime - startTime));
                        return result;
                    }
                    return originalMethod.invoke(obj, args);  // Если аннотации нет, просто вызываем метод
                }
            });
        }
        return obj;
    }

    private boolean hasBenchMarkAnnotation(Method method) {
        return method.isAnnotationPresent(Benchmark.class);
    }
}
