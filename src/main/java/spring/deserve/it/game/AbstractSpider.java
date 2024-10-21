package spring.deserve.it.game;

import lombok.Getter;
import lombok.Setter;
import spring.deserve.it.api.Spider;
import spring.deserve.it.common.InjectProperty;

@Getter
@Setter
public abstract class AbstractSpider implements Spider {

    @InjectProperty(propertyName = "spider.default.lives")
    private int lives;

    public boolean isAlive() {
        return lives > 0;
    }
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }
}

