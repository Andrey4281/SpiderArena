package spring.deserve.starter.config;

import org.reflections.Reflections;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.deserve.starter.annotainon.Singleton;

import java.util.Optional;
import java.util.Set;

public class ImportBeanDefinitionSingletoneRegistrar implements ImportBeanDefinitionRegistrar {

    private Environment environment;

    public ImportBeanDefinitionSingletoneRegistrar(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Reflections reflections = new Reflections(getPackageToScan(registry));
        Set<Class<?>> singletones = reflections.getTypesAnnotatedWith(Singleton.class);
        singletones.forEach(clazz -> {
            String simpleName = clazz.getSimpleName();
            String beanName = simpleName.replaceFirst(simpleName.substring(0, 1), simpleName.substring(0, 1).toLowerCase());
            if (!registry.containsBeanDefinition(beanName)) {
                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                genericBeanDefinition.setBeanClass(clazz);
                registry.registerBeanDefinition(beanName, genericBeanDefinition);
            }
        });
    }

    private String getPackageToScan(BeanDefinitionRegistry registry) {
        return Optional.ofNullable(environment.getProperty("package.singletone")).orElseGet(() -> {
            String[] beanNamesForAnnotation = ((DefaultListableBeanFactory) registry).getBeanNamesForAnnotation(SpringBootApplication.class);
            if (beanNamesForAnnotation.length >= 1) {
                String beanDefinitionName = beanNamesForAnnotation[0];
                try {
                    return Class.forName(registry.getBeanDefinition(beanDefinitionName).getBeanClassName()).getPackage().getName();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            throw new RuntimeException("ооо");
        });
    }
}
