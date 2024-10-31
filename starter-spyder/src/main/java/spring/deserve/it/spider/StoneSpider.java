package spring.deserve.it.spider;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.Benchmark;
import spring.deserve.starter.annotainon.PlayerQualifier;
import spring.deserve.starter.spider.AbstractSpider;
import spring.deserve.starter.spider.RPSEnum;
import spring.deserve.starter.spider.Spider;

@Component
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
@PlayerQualifier("Kirill")
public class StoneSpider extends AbstractSpider implements Spider {


    @Benchmark
    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.ROCK;
    }

    @PreDestroy
    public void closeAll(){
        System.out.println("Все что важно я закрыл, а что не важно и хрен с ним");
    }
}
