package spring.deserve.it.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.List;

public class SpiderLifePointCut extends DynamicMethodMatcherPointcut {
    private final List<String> pattern = List.of("Paper");

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return "loseLife".equals(method.getName());
    }

    @Override
    public ClassFilter getClassFilter() {
        return clazz -> pattern.stream().anyMatch(p -> clazz.getSimpleName().contains(p));
    }
}
