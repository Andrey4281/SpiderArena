package spring.deserve.it.infra;

import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.common.Singleton;
import spring.deserve.it.game.AbstractSpider;

@Singleton
public class TestSpiderSingleton extends AbstractSpider {
        @Override
        public RPSEnum fight() {
            return null;
        }

    }