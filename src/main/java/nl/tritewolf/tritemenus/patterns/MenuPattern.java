package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.iterators.MenuIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MenuPattern {

    @NotNull List<@NotNull String> getPattern();

    void handle(@NotNull MenuIterator menuIterator);
}
