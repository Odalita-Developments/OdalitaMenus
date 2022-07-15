package nl.tritewolf.tritemenus.patterns;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface PatternCache<CacheType> {

    @NotNull CacheType getCache();
}