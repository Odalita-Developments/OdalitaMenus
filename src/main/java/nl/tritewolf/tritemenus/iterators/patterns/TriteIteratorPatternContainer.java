package nl.tritewolf.tritemenus.iterators.patterns;

import java.util.HashMap;
import java.util.Map;

public class TriteIteratorPatternContainer {

    private final Map<Class<? extends TriteIteratorPattern>, TriteIteratorPattern> patterns = new HashMap<>();

    public void addPattern(TriteIteratorPattern iteratorPattern) {
        patterns.putIfAbsent(iteratorPattern.getClass(), iteratorPattern);
    }

    public TriteIteratorPattern getIteratorPatternByCass(Class<? extends TriteIteratorPattern> itClass) {
        return patterns.getOrDefault(itClass, null);
    }


}
