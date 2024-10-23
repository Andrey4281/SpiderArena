package spring.deserve.it.common;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.game.AbstractSpider;
import spring.deserve.it.game.StatisticalSpider;
import spring.deserve.it.service.HistoricalService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InjectObjectConfiguratorTest {



    @Test
    @SneakyThrows
    void should_inject_object_into_field() {
        StatisticalSpider statisticalSpider = new StatisticalSpider();
        InjectObjectConfigurator injectObjectConfigurator = new InjectObjectConfigurator();

        injectObjectConfigurator.configure(statisticalSpider);

        assertThat(statisticalSpider.getHistoricalService()).isNotNull();
    }

    @Test
    @SneakyThrows
    void should_inject_object_into_field_by_setter() {
        TestSpiderWithInjectBySetter statisticalSpider = new TestSpiderWithInjectBySetter();
        InjectObjectConfigurator injectObjectConfigurator = new InjectObjectConfigurator();

        injectObjectConfigurator.configure(statisticalSpider);

        assertThat(statisticalSpider.getValue()).isNotNull();
    }


    private static class TestSpiderWithInjectBySetter extends AbstractSpider {
        @Getter
        private HistoricalService value;

        @Override
        public RPSEnum fight() {
            return null;
        }

        @Inject
        public void setValue(HistoricalService value) {
            this.value = value;
        }
    }
}