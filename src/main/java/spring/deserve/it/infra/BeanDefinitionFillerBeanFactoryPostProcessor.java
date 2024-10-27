package spring.deserve.it.infra;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.MethodMetadata;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class BeanDefinitionFillerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final List<String> FIXED_BEAN_DEFINITION = List.of("configPaperSpider");

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Arrays.stream(beanFactory.getBeanDefinitionNames())
                .filter(bd -> beanFactory.getBeanDefinition(bd).getBeanClassName() == null && FIXED_BEAN_DEFINITION.contains(bd))
                .map(beanFactory::getBeanDefinition)
                .forEach(beanDefinition -> {
                    MethodMetadata methodMetadata = (MethodMetadata) beanDefinition.getSource();
                    if (methodMetadata != null) {
                        try {
                            Class<?> configClass = Class.forName(methodMetadata.getDeclaringClassName());
                            Object configClassInstance = configClass.getDeclaredConstructor().newInstance();
                            Optional<Method> beanMethodOptional = Arrays.stream(configClass.getMethods()).filter(m ->
                                    m.getName().equals(methodMetadata.getMethodName())).findFirst();
                            if (beanMethodOptional.isPresent()) {
                                Method method = beanMethodOptional.get();
                                method.setAccessible(true);
                                Object targetBean = method.invoke(configClassInstance);
                                beanDefinition.setBeanClassName(targetBean.getClass().getName());
                            }
                        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException | NoSuchMethodException e) {
                            System.out.println("Что то пошло не так");
                        }
                    }
                });
    }
}
