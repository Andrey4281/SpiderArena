package spring.deserve.it.spider;

import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.PlayerQualifier;
import spring.deserve.starter.spider.AbstractSpider;
import spring.deserve.starter.spider.RPSEnum;
import spring.deserve.starter.spider.Spider;

@Component
//@Scope(scopeName = "prototype")
@PlayerQualifier("Kirill2")
public class ScissorsSpider extends AbstractSpider implements Spider {
    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return  RPSEnum.SCISSORS;
    }
}
