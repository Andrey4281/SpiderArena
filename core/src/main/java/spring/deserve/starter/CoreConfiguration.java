package spring.deserve.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import spring.deserve.starter.config.ImportBeanDefinitionSingletoneRegistrar;

@AutoConfiguration
@ComponentScan("spring.deserve.starter")
@Import(value = {
        ImportBeanDefinitionSingletoneRegistrar.class
})
public class CoreConfiguration {
}
