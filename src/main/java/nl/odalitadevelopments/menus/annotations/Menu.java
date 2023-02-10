package nl.odalitadevelopments.menus.annotations;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {

    String title();

    byte rows() default 3;

    @ApiStatus.Experimental
    InventoryType type() default InventoryType.CHEST;

    String globalCacheKey() default "";
}