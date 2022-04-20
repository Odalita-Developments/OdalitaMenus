package nl.tritewolf.patterns;

import nl.tritewolf.tritemenus.annotations.Pattern;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;

import java.util.HashMap;
import java.util.Map;

public final class IteratorPatternContainer {

    private final Map<Class<? extends IteratorPattern>, IteratorPattern> tritePatterns = new HashMap<>();

    public void registerPattern(IteratorPattern iteratorPattern) throws MissingInitializationsAnnotationException {
        this.isPattern(iteratorPattern.getClass().isAnnotationPresent(Pattern.class));
        this.tritePatterns.putIfAbsent(iteratorPattern.getClass(), iteratorPattern);
    }

    public void unregisterPattern(IteratorPattern iteratorPattern) throws MissingInitializationsAnnotationException {
        isPattern(iteratorPattern.getClass().isAnnotationPresent(Pattern.class));
        this.tritePatterns.remove(iteratorPattern.getClass());
    }

    public IteratorPattern getIteratorPatternByClass(Class<? extends IteratorPattern> clazz) {
        return this.tritePatterns.get(clazz);
    }

    private void isPattern(boolean triteIteratorPattern) throws MissingInitializationsAnnotationException {
        if (!triteIteratorPattern) {
            throw new MissingInitializationsAnnotationException(Pattern.class, "pattern");
        }
    }
}