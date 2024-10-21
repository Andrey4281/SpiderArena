package spring.deserve.it.common;

import org.junit.jupiter.api.Test;
import spring.deserve.it.game.StoneSpider;

import static org.junit.jupiter.api.Assertions.*;

class InjectPropertyObjectConfiguratorTest {

    @Test
    public void testInjectProperty() throws Exception {
        // Создаем объект паука
        StoneSpider spider = new StoneSpider();

        // Загружаем проперти и инжектируем их в паука
        InjectPropertyObjectConfigurator loader = new InjectPropertyObjectConfigurator();
        loader.configure(spider);

        // Проверяем, что количество жизней было корректно заинжектировано
        assertEquals(5, spider.getLives());  // Допустим, в application.properties стоит "spider.default.lives=5"
    }
}