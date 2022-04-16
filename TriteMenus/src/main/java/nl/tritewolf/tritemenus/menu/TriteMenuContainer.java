package nl.tritewolf.tritemenus.menu;

import lombok.val;
import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;

import java.util.ArrayList;
import java.util.List;

public class TriteMenuContainer {

    private final List<TriteMenuProvider> triteMenus = new ArrayList<>();

    public void addMenu(TriteMenuProvider triteMenuProvider) throws MissingInitializationsAnnotationException {
        val isMenu = triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class);
        if (!isMenu) throw new MissingInitializationsAnnotationException();

        triteMenus.add(triteMenuProvider);
    }

    public void removeMenu(TriteMenuProvider triteMenuProvider) throws Exception {
        boolean isMenu = triteMenuProvider.getClass().isAnnotationPresent(TriteMenu.class);
        if (!isMenu) throw new MissingInitializationsAnnotationException();


        val removed = triteMenus.remove(triteMenuProvider);
        if (!removed) throw new Exception("Could not remove menu");
    }
}
