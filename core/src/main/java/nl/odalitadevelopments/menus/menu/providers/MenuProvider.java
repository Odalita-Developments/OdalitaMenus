package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.annotations.Menu;
import org.jetbrains.annotations.NotNull;

public interface MenuProvider {

    @NotNull
    default Menu getMenu() {
        if (!this.getClass().isAnnotationPresent(Menu.class))
            throw new IllegalStateException("MenuProvider '" + getClass().getSimpleName() + "' is not annotated with @Menu");
        return this.getClass().getAnnotation(Menu.class);
    }
}