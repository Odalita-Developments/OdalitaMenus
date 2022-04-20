package nl.tritewolf.tritemenus.annotations;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.iterators.patterns.TriteIteratorPatternContainer;
import nl.tritewolf.tritemenus.patterns.IteratorPattern;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationBinding implements TypeReporter {

    @TriteJect
    private TriteIteratorPatternContainer iteratorPatternContainer;

    @TriteJect
    private TriteMenuContainer triteMenuContainer;

    private final List<String> classNames = new ArrayList<>();

    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
        try {
            if (this.classNames.contains(className)) return;

            boolean addClassName = false;
            if (annotation.equals(Menu.class)) {
                addClassName = true;

                Class<?> menuClass = Class.forName(className);
                if (MenuProvider.class.isAssignableFrom(menuClass)) {
                    this.triteMenuContainer.registerMenu((MenuProvider) menuClass.getDeclaredConstructor().newInstance());
                }
            }

            if (annotation.equals(Pattern.class)) {
                addClassName = true;

                Class<?> menuClass = Class.forName(className);
                if (IteratorPattern.class.isAssignableFrom(menuClass)) {
                    this.iteratorPatternContainer.registerPattern((IteratorPattern) menuClass.getDeclaredConstructor().newInstance());
                }
            }

            if (addClassName) {
                this.classNames.add(className);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] annotations() {
        return new Class[]{Menu.class, Pattern.class};
    }
}