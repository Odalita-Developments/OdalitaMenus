package nl.tritewolf.tritemenus.annotations;

import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuFrame {

    @Range(from = 1, to = 9)
    int width() default 1;

    @Range(from = 1, to = 6)
    int height() default 1;
}