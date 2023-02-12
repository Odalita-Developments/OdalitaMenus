package nl.odalitadevelopments.menus.patterns;

import nl.odalitadevelopments.menus.iterators.MenuIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MenuPattern<CacheType> extends PatternCache<CacheType> {

    @NotNull List<@NotNull String> getPattern();

    void handle(@NotNull MenuIterator menuIterator);
}
