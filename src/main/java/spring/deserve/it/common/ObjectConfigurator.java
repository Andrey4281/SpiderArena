package spring.deserve.it.common;

import spring.deserve.it.infra.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object obj) throws Exception;

    default void setContext(ApplicationContext context) {

    }
}
