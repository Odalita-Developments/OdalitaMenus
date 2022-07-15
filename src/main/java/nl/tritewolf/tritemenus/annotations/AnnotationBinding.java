package nl.tritewolf.tritemenus.annotations;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import nl.tritewolf.tritemenus.patterns.PatternContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationBinding implements TypeReporter {

    @TriteJect
    private PatternContainer patternContainer;

    private final List<String> classNames = new ArrayList<>();

    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
        try {
            if (annotation.equals(Pattern.class) && !this.classNames.contains(className)) {
                this.classNames.add(className);

                Class<?> patternClass = Class.forName(className);
                if (MenuPattern.class.isAssignableFrom(patternClass)) {
                    Constructor<?> declaredConstructor = patternClass.getDeclaredConstructor();
                    declaredConstructor.setAccessible(true);

                    this.patternContainer.registerPattern((MenuPattern<?>) declaredConstructor.newInstance());
                    declaredConstructor.setAccessible(false);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] annotations() {
        return new Class[]{Pattern.class};
    }
}