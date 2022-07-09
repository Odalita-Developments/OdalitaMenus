package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.SlotPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PatternContainer {

    private final Map<Class<? extends IteratorPattern>, IteratorPattern> patterns = new HashMap<>();
    private final Map<Class<? extends DirectionPattern>, List<SlotPos>> directionPatterns = new HashMap<>();

    public void registerIteratorPattern(@NotNull IteratorPattern iteratorPattern) {
        this.patterns.putIfAbsent(iteratorPattern.getClass(), iteratorPattern);
    }

    public void registerDirectionsPattern(@NotNull DirectionPattern iteratorPattern) {
        List<SlotPos> slots = iteratorPattern.getSlots();
        this.directionPatterns.put(iteratorPattern.getClass(), slots);
    }

    public @Nullable IteratorPattern getIteratorPatternByClass(@NotNull Class<? extends IteratorPattern> itClass) {
        return this.patterns.get(itClass);
    }

    public @Nullable List<@NotNull SlotPos> getDirectionsPatternByClass(@NotNull Class<? extends DirectionPattern> itClass) {
        return this.directionPatterns.get(itClass);
    }
}