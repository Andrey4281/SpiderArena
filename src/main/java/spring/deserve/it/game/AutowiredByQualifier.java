package spring.deserve.it.game;


public @interface AutowiredByQualifier {
    String qualifierName() default "PlayerQualifier";
}
