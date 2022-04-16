package nl.tritewolf.tritemenus.menu;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.annotations.TriteMenu;

import java.lang.annotation.Annotation;

public class TriteMenuBinding implements TypeReporter {

    @TriteJect
    private TriteMenuContainer triteMenuContainer;

    @SuppressWarnings("unchecked")
    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
        try {
            Class<? extends TriteMenuProvider> clazz = (Class<? extends TriteMenuProvider>) Class.forName(className);
            triteMenuContainer.addMenu(clazz.getDeclaredConstructor().newInstance());
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
