package nl.tritewolf.tritemenus.patterns;

import java.util.HashMap;
import java.util.Map;

public class IteratorPatternContainer {

    private final Map<Class<? extends IteratorPattern>, IteratorPattern> patterns = new HashMap<>();

    public void addPattern(IteratorPattern iteratorPattern) {
        patterns.putIfAbsent(iteratorPattern.getClass(), iteratorPattern);
    }

    public IteratorPattern getIteratorPatternByCass(Class<? extends IteratorPattern> itClass) {
        return patterns.getOrDefault(itClass, null);
    }


}
