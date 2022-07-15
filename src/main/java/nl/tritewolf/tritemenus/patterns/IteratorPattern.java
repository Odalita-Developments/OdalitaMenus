package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import org.jetbrains.annotations.NotNull;

public interface IteratorPattern extends MenuPattern<IteratorPattern> {

    @Override
    default @NotNull IteratorPattern getCache() {
        return this;
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
        for (int row = 0; row < getPattern().size(); row++) {
            String patternLine = getPattern().get(row);

            for (int column = 0; column < patternLine.length(); column++) {
                if (patternLine.charAt(column) == '#') {
                    menuIterator.blacklist(SlotPos.of(row, column).getSlot());
                }
            }
        }
    }
}
