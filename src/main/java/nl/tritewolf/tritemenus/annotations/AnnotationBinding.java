package nl.tritewolf.tritemenus.annotations;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritejection.utils.types.TypeReporter;
import nl.tritewolf.tritemenus.patterns.DirectionPattern;
import nl.tritewolf.tritemenus.patterns.IteratorPattern;
import nl.tritewolf.tritemenus.patterns.PatternContainer;

import java.lang.annotation.Annotation;
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
                if (IteratorPattern.class.isAssignableFrom(patternClass)) {
                    this.patternContainer.registerIteratorPattern((IteratorPattern) patternClass.getDeclaredConstructor().newInstance());
                }

                if (DirectionPattern.class.isAssignableFrom(patternClass)) {
                    this.patternContainer.registerDirectionsPattern((DirectionPattern) patternClass.getDeclaredConstructor().newInstance());
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