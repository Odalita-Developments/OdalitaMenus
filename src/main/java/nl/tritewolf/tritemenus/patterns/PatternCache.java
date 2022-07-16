package nl.tritewolf.tritemenus.patterns;

import org.jetbrains.annotations.NotNull;

public sealed interface PatternCache<CacheType> permits MenuPattern {

    @NotNull CacheType getCache();
}