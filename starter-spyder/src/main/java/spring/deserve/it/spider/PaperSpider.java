package spring.deserve.it.spider;

import org.springframework.stereotype.Component;
import spring.deserve.starter.annotainon.PlayerQualifier;
import spring.deserve.starter.spider.AbstractSpider;
import spring.deserve.starter.spider.RPSEnum;
import spring.deserve.starter.spider.Spider;


@Component
@PlayerQualifier("Kirill1")
public class PaperSpider extends AbstractSpider implements Spider {

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.PAPER;
    }
}
