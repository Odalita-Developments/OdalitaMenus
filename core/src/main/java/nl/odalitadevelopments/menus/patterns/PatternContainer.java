package nl.odalitadevelopments.menus.patterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public final class PatternContainer {

    private final Map<Class<? extends MenuPattern>, Object> menuPatterns = new HashMap<>();

    public <CacheType, T extends MenuPattern<CacheType>> void registerPattern(@NotNull T pattern) {
        this.menuPatterns.putIfAbsent(pattern.getClass(), pattern.getCache());
    }

    @SuppressWarnings("unchecked")
    public <CacheType, T extends MenuPattern<CacheType>> @Nullable CacheType getPattern(@NotNull Class<T> patternClass) {
        Object patternCache = this.menuPatterns.get(patternClass);
        if (patternCache == null) return null;

        try {
            return (CacheType) patternCache;
        } catch (ClassCastException e) {
            return null;
        }
    }
}