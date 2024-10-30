package spring.deserve.starter.spider;

import lombok.Getter;
import lombok.Setter;
import spring.deserve.starter.annotainon.InjectProperty;

@Getter
@Setter
public abstract class AbstractSpider implements Spider {

    private String owner;

    @InjectProperty("spider.default.lives")
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

