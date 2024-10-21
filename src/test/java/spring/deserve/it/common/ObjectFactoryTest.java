package spring.deserve.it.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import spring.deserve.it.game.PaperSpider;

class ObjectFactoryTest {

    @Test
    void createObject() {
        PaperSpider spider = ObjectFactory.getInstance().createObject(PaperSpider.class);
        assertEquals(5, spider.getLives());
    }
}