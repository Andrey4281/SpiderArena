package spring.deserve.it.game;

public interface ProxyConfigurator {
    Object configureProxy(Object t, Class implClass);

    default void setApplicationContext(ApplicationContext context) {
    }
}
