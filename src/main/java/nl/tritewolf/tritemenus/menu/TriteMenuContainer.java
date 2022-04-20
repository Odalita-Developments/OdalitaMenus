package nl.tritewolf.tritemenus.menu;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;

import java.util.HashMap;
import java.util.Map;

public final class TriteMenuContainer {

    private final Map<Class<? extends TriteMenuProvider>, TriteMenuProvider> triteMenus = new HashMap<>();

    public void registerMenu(TriteMenuProvider triteMenuProvider) throws MissingInitializationsAnnotationException {
        this.isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));
        this.triteMenus.putIfAbsent(triteMenuProvider.getClass(), triteMenuProvider);
    }

    public void unregisterMenu(TriteMenuProvider triteMenuProvider) throws MissingInitializationsAnnotationException {
        isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));
        this.triteMenus.remove(triteMenuProvider.getClass());
    }

    public TriteMenuProvider getMenuProviderByClass(Class<? extends TriteMenuProvider> clazz) {
        return this.triteMenus.get(clazz);
    }

    private void isMenu(boolean triteMenuProvider) throws MissingInitializationsAnnotationException {
        if (!triteMenuProvider) {
            throw new MissingInitializationsAnnotationException(TriteMenu.class, "menu");
        }
    }
}