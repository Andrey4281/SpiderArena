package spring.deserve.it.common;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import spring.deserve.it.game.StatisticalSpider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InjectObjectConfiguratorTest {

    @Test
    @SneakyThrows
    void should_inject_historical_service() {
        StatisticalSpider statisticalSpider = new StatisticalSpider();
        InjectObjectConfigurator injectObjectConfigurator = new InjectObjectConfigurator();

        injectObjectConfigurator.configure(statisticalSpider);

        assertThat(statisticalSpider.getHistoricalService()).isNotNull();
    }
}