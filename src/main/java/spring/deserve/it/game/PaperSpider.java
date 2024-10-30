package spring.deserve.it.game;

import org.springframework.stereotype.Component;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;
import spring.deserve.it.infra.DefaultSpider;

@Component
@DefaultSpider
@PlayerQualifier("Kirill")
public class PaperSpider extends AbstractSpider {

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.PAPER;
    }
}
