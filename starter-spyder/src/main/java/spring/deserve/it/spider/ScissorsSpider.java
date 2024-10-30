package spring.deserve.it.spider;

import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.PlayerQualifier;
import spring.deserve.starter.spider.AbstractSpider;
import spring.deserve.starter.spider.RPSEnum;
import spring.deserve.starter.spider.Spider;

@Component
//@Scope(scopeName = "prototype")
@PlayerQualifier("Kirill")
public class ScissorsSpider extends AbstractSpider {
    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return  RPSEnum.SCISSORS;
    }
}
