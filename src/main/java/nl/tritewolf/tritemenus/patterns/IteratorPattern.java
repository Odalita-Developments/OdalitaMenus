package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import org.jetbrains.annotations.NotNull;

public interface IteratorPattern extends MenuPattern<IteratorPattern> {

    @Override
    default @NotNull IteratorPattern getCache() {
        return this;
    }

    default int startingRow(){
        return 0;
    }

    default int startingColumn(){
        return 0;
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
        for (int row = startingRow(); row < getPattern().size(); row++) {
            String patternLine = getPattern().get(row);

            for (int column = startingColumn(); column < patternLine.length(); column++) {
                if (patternLine.charAt(column) == '#') {
                    menuIterator.blacklist(SlotPos.of((row + startingRow()), (column + startingColumn())).getSlot());
                }
            }
        }
    }
}
