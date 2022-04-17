package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.val;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TriteMenuContainer {


    @TriteJect
    private TriteInventoryContents triteInventoryContents;

    private final Map<Class<?>, Pair<TriteMenuProvider, TriteMenuObject>> triteMenus = new HashMap<>();
    private final Map<Class<?>, Pair<TriteGlobalMenuProvider, TriteMenuObject>> triteGlobalMenus = new HashMap<>();

    public void addMenu(TriteMenuProvider triteMenuProvider) throws MissingInitializationsAnnotationException {
        isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));

        val annotation = triteMenuProvider.getClass().getAnnotation(TriteMenu.class);
        if (annotation.menuType().equals(TriteMenuType.GLOBAL)) {
            //todo THROW EXEPTION
            return;
        }

        val triteMenuObject = new TriteMenuObject(annotation.rows(), annotation.displayName(), annotation.menuType());
        triteMenus.putIfAbsent(triteMenuProvider.getClass(), new Pair<>(triteMenuProvider, triteMenuObject));
    }

    public void addGlobalMenu(TriteGlobalMenuProvider triteMenuProvider) throws MissingInitializationsAnnotationException {
        isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));

        val annotation = triteMenuProvider.getClass().getAnnotation(TriteMenu.class);
        if (annotation.menuType().equals(TriteMenuType.PLAYER)) {
            //todo THROW EXEPTION
            return;
        }
        val triteMenuObject = new TriteMenuObject(annotation.rows(), annotation.displayName(), annotation.menuType());
        triteGlobalMenus.putIfAbsent(triteMenuProvider.getClass(), new Pair<>(triteMenuProvider, triteMenuObject));
    }

    private void isMenu(boolean triteMenuProvider) throws MissingInitializationsAnnotationException {
        if (!triteMenuProvider) throw new MissingInitializationsAnnotationException();
    }

    public void removeMenu(TriteMenuProvider triteMenuProvider) throws Exception {
        isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));

        if (!(triteMenus.containsKey(triteMenuProvider))) throw new Exception("Could not remove menu");
        triteMenus.remove(triteMenuProvider);
    }

    public void removeGlobalMenu(TriteGlobalMenuProvider triteMenuProvider) throws Exception {
        isMenu(triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class));

        if (!(triteGlobalMenus.containsKey(triteMenuProvider))) throw new Exception("Could not remove menu");
        triteGlobalMenus.remove(triteMenuProvider);
    }
}
