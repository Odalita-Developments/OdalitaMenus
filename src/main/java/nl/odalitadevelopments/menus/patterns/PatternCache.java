package nl.odalitadevelopments.menus.patterns;

import org.jetbrains.annotations.NotNull;

public interface PatternCache<CacheType> {

    @NotNull CacheType getCache();
}