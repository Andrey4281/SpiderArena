package spring.deserve.it.infra;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ApplicationContextTest {

    @Test
    void should_create_singleton() {
        ApplicationContext applicationContext = new ApplicationContext("");

        Object spider1 = applicationContext.getObject(TestSpiderSingleton.class);
        Object spider2 = applicationContext.getObject(TestSpiderSingleton.class);

        assertEquals(spider1, spider2);
    }
    @Test
    void should_create_prototype() {
        ApplicationContext applicationContext = new ApplicationContext("");

        Object object1 = applicationContext.getObject(this.getClass());
        Object object2 = applicationContext.getObject(this.getClass());

        assertNotEquals(object1, object2);
    }

}