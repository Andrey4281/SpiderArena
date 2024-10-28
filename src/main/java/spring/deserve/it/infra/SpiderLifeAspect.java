package spring.deserve.it.infra;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import spring.deserve.it.api.Spider;

import java.util.List;

//@Aspect
//@Component
public class SpiderLifeAspect {

    private final List<String> pattern = List.of("Paper");

    @Around("execution( * spring.deserve.it.api.Spider.loseLife())")
    public Object aroundLoseLife(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object result = joinPoint.proceed();

        boolean isImmortalSpider = pattern.stream().anyMatch(className::contains);
        if (isImmortalSpider) {
            Spider target = (Spider) joinPoint.getTarget();
            if (target.getLives() == 1) {
                target.setLives(10);
            }
        }
        return result;
    }

}
