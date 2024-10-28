package spring.deserve.it.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodProxy;
import spring.deserve.it.api.Spider;

import java.lang.reflect.Method;

public class SpiderLifeMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Spider target = (Spider) invocation.getThis();
        if (target.getLives() == 1) {
            target.setLives(10);
        }
        return invocation.proceed();
    }
}
