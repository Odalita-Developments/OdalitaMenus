package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.annotations.Menu;
import org.jetbrains.annotations.NotNull;

public interface MenuProvider {

    @NotNull
    default Menu getMenu() {
        if (!getClass().isAnnotationPresent(Menu.class))
            throw new IllegalStateException("MenuProvider '" + getClass().getSimpleName() + "' is not annotated with @Menu");
        return getClass().getAnnotation(Menu.class);
    }

}