package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.iterators.MenuIterator;

public interface IteratorPattern extends MenuPattern {

    @Override
    default void handle(MenuIterator menuIterator) {
        for (int row = 0; row < getPattern().size(); row++) {
            for (int column = 0; column < getPattern().get(row).length(); column++) {
                if (getPattern().get(row).charAt(column) == '#') {
                    menuIterator.blacklist(SlotPos.of(row, column).getSlot());
                }
            }
        }
    }
}
