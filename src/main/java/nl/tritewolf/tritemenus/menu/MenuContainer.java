package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class MenuContainer {

    private final Map<Class<? extends MenuProvider>, MenuProvider> triteMenus = new HashMap<>();

    public void registerMenu(MenuProvider menuProvider) throws MissingInitializationsAnnotationException {
        this.isMenu(menuProvider.getClass().isAnnotationPresent(Menu.class));
        this.triteMenus.putIfAbsent(menuProvider.getClass(), menuProvider);
    }

    public void unregisterMenu(MenuProvider menuProvider) throws MissingInitializationsAnnotationException {
        isMenu(menuProvider.getClass().isAnnotationPresent(Menu.class));
        this.triteMenus.remove(menuProvider.getClass());
    }

    public MenuProvider getMenuProviderByClass(Class<? extends MenuProvider> clazz) {
        return this.triteMenus.get(clazz);
    }

    private void isMenu(boolean triteMenuProvider) throws MissingInitializationsAnnotationException {
        if (!triteMenuProvider) {
            throw new MissingInitializationsAnnotationException(Menu.class, "menu");
        }
    }
}