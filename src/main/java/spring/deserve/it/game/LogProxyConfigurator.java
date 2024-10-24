package spring.deserve.it.game;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.reflections.ReflectionUtils.withAnnotation;

public class LogProxyConfigurator implements ProxyConfigurator {

    @Override
    public Object configureProxy(Object originalObject, Class implClass) {
        boolean isLoggedClass = ReflectionUtils.getAllMethods(implClass).stream()
                .anyMatch(m -> m.isAnnotationPresent(Log.class));

        Map<String, String[]> methodNameToLoggedParameters = ReflectionUtils.getAllMethods(implClass,
                withAnnotation(Log.class)).stream().collect(Collectors.toMap(Method::getName, m -> m.getAnnotation(Log.class).value()));

        if (isLoggedClass) {
            if (implClass.getInterfaces().length != 0) {
                Object proxy = newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(), (o, method, args) -> {
                    if (methodNameToLoggedParameters.containsKey(method.getName())) {
                        String[] loggedParameters = methodNameToLoggedParameters.get(method.getName());
                        Map<String, Integer> argsNameToIndex = getArgsNameToIndex(method, loggedParameters);
                        String argsString = argsNameToIndex.entrySet().stream().map(e -> e.getKey() + " = " +
                                args[e.getValue()]).collect(Collectors.joining(","));
                        System.out.println("Invoke method " + method.getName() + " " + argsString);
                        return method.invoke(originalObject, args);
                    }
                    return method.invoke(originalObject, args);
                });
                return proxy;
            } else {
                Object proxy = Enhancer.create(implClass, (InvocationHandler) (o, method, args) -> {
                    if (methodNameToLoggedParameters.containsKey(method.getName())) {
                        String[] loggedParameters = methodNameToLoggedParameters.get(method.getName());
                        Map<String, Integer> argsNameToIndex = getArgsNameToIndex(method, loggedParameters);
                        String argsString = argsNameToIndex.entrySet().stream().map(e -> e.getKey() + " = " +
                                args[e.getValue()]).collect(Collectors.joining(","));
                        System.out.println("Invoke method " + method.getName() + " " + argsString);
                        return method.invoke(originalObject, args);
                    }
                    return method.invoke(originalObject, args);
                });
                return proxy;
            }
        }

        return originalObject;
    }

    private static Map<String, Integer> getArgsNameToIndex(Method method, String[] loggedParameters) {
        Map<String, Integer> parametersByIndexInSignature = new HashMap<>();
        Integer counter = 0;
        for (Parameter parameter : method.getParameters()) {
            parametersByIndexInSignature.put(parameter.getName(), counter);
            counter++;
        }
        Map<String, Integer> argsNameToIndex = new HashMap<>();
        for (String loggedParameter : loggedParameters) {
            if (parametersByIndexInSignature.containsKey(loggedParameter)) {
                argsNameToIndex.put(loggedParameter, parametersByIndexInSignature.get(loggedParameter));
            } else {
                throw new RuntimeException("Invalid paraeter name for logging");
            }
        }
        return argsNameToIndex;
    }
}
