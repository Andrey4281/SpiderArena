package spring.deserve.it.aop;


import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpiderLifeConfig {

    @Bean
    public DefaultPointcutAdvisor spiderLifeAdvisor() {
        return new DefaultPointcutAdvisor(new SpiderLifePointCut(), new SpiderLifeMethodInterceptor());
    }
}
