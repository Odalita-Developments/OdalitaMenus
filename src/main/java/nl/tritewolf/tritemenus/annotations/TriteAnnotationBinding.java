package nl.tritewolf.tritemenus.annotations;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.iterators.patterns.TriteIteratorPattern;
import nl.tritewolf.tritemenus.iterators.patterns.TriteIteratorPatternContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public final class TriteAnnotationBinding implements TypeReporter {

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
            if (annotation.equals(TriteMenu.class)) {
                addClassName = true;

                Class<?> menuClass = Class.forName(className);
                if (TriteMenuProvider.class.isAssignableFrom(menuClass)) {
                    this.triteMenuContainer.registerMenu((TriteMenuProvider) menuClass.getDeclaredConstructor().newInstance());
                }
            }

            if (annotation.equals(TritePattern.class)) {
                addClassName = true;

                Class<?> menuClass = Class.forName(className);
                if (TriteIteratorPattern.class.isAssignableFrom(menuClass)) {
                    this.iteratorPatternContainer.registerPattern((TriteIteratorPattern) menuClass.getDeclaredConstructor().newInstance());
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
        return new Class[]{TriteMenu.class, TritePattern.class};
    }
}