package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.SlotPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternContainer {

    private final Map<Class<? extends IteratorPattern>, IteratorPattern> patterns = new HashMap<>();
    private final Map<Class<? extends DirectionPattern>, List<SlotPos>> directionPatterns = new HashMap<>();

    public void registerIteratorPattern(IteratorPattern iteratorPattern) {
        patterns.putIfAbsent(iteratorPattern.getClass(), iteratorPattern);
    }

    public void registerDirectionsPattern(DirectionPattern iteratorPattern) {
        List<SlotPos> slots = iteratorPattern.getSlots();
        directionPatterns.put(iteratorPattern.getClass(), slots);
    }

    public IteratorPattern getIteratorPatternByClass(Class<? extends IteratorPattern> itClass) {
        return patterns.getOrDefault(itClass, null);
    }

    public List<SlotPos> getDirectionsPatternByClass(Class<? extends DirectionPattern> itClass) {
        return directionPatterns.getOrDefault(itClass, null);
    }


}
