package nl.tritewolf.tritemenus.iterators.patterns;

import nl.tritewolf.tritemenus.annotations.TritePattern;
import nl.tritewolf.tritemenus.exceptions.MissingInitializationsAnnotationException;

import java.util.HashMap;
import java.util.Map;

public final class TriteIteratorPatternContainer {

    private final Map<Class<? extends TriteIteratorPattern>, TriteIteratorPattern> tritePatterns = new HashMap<>();

    public void registerPattern(TriteIteratorPattern triteIteratorPattern) throws MissingInitializationsAnnotationException {
        this.isPattern(triteIteratorPattern.getClass().isAnnotationPresent(TritePattern.class));
        this.tritePatterns.putIfAbsent(triteIteratorPattern.getClass(), triteIteratorPattern);
    }

    public void unregisterPattern(TriteIteratorPattern triteIteratorPattern) throws MissingInitializationsAnnotationException {
        isPattern(triteIteratorPattern.getClass().isAnnotationPresent(TritePattern.class));
        this.tritePatterns.remove(triteIteratorPattern.getClass());
    }

    public TriteIteratorPattern getIteratorPatternByClass(Class<? extends TriteIteratorPattern> clazz) {
        return this.tritePatterns.get(clazz);
    }

    private void isPattern(boolean triteIteratorPattern) throws MissingInitializationsAnnotationException {
        if (!triteIteratorPattern) {
            throw new MissingInitializationsAnnotationException(TritePattern.class, "pattern");
        }
    }
}