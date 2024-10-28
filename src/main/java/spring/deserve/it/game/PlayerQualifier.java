package spring.deserve.it.game;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerQualifier {
    @AliasFor(annotation = Qualifier.class, attribute = "value")
    String value();
}
