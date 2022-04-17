package nl.tritewolf.tritemenus.menu;

import lombok.val;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriteMenuBinding implements TypeReporter {

    @TriteJect
    private TriteMenuContainer triteMenuContainer;

    private final List<String> classNames = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
        try {
            if (annotation.equals(TriteMenu.class) && !classNames.contains(className)) {
                classNames.add(className);
                TriteMenu menu = Class.forName(className).getAnnotation(TriteMenu.class);
                if (menu.menuType().equals(TriteMenuType.GLOBAL)) {
                    Class<? extends TriteGlobalMenuProvider> clazz = (Class<? extends TriteGlobalMenuProvider>) Class.forName(className);
                    triteMenuContainer.addGlobalMenu(clazz.getDeclaredConstructor().newInstance());
                    return;
                }

                Class<? extends TriteMenuProvider> clazzA = (Class<? extends TriteMenuProvider>) Class.forName(className);
                triteMenuContainer.addMenu(clazzA.getDeclaredConstructor().newInstance());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] annotations() {
        return new Class[]{TriteMenu.class};
    }
}
