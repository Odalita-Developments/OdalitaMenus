package nl.odalitadevelopments.menus.annotations;

import nl.odalitadevelopments.menus.menu.type.MenuType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {

    String id() default "";

    String title();

    MenuType type() default MenuType.CHEST_3_ROW;

    String globalCacheKey() default "";
}