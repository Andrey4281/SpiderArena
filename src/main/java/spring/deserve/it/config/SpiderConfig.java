package spring.deserve.it.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.deserve.it.game.PaperSpider;

@Configuration
public class SpiderConfig {

    @Bean
    public PaperSpider configPaperSpider() {
       return new PaperSpider();
    }
}
