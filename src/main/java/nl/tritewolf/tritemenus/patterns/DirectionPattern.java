package nl.tritewolf.tritemenus.patterns;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public interface DirectionPattern extends MenuPattern {

    boolean shouldContinuePattern();

    MenuIteratorType menuIteratorType();

    default List<SlotPos> getSlots() {
        TreeMap<Integer, SlotPos> slots = new TreeMap<>();

        for (int row = 0; row < getPattern().size(); row++) {
            String patternLine = getPattern().get(row);
            int lastIndex = 0;

            for (int column = 0; column < patternLine.length(); column++) {
                if (lastIndex >= patternLine.length()) continue;

                String string = patternLine.substring(lastIndex, lastIndex += 2);
                if (NumberUtils.isDigits(string)) {
                    slots.put(Integer.parseInt(string), SlotPos.of(row, column));
                }
            }
        }

        if (!this.shouldContinuePattern() || this.menuIteratorType() == null) {
            return new ArrayList<>(slots.values());
        }

        List<SlotPos> newSlotPositions = new ArrayList<>(slots.values());
        SlotPos highestPos = newSlotPositions.stream()
                .max(Comparator.comparing(SlotPos::getColumn))
                .orElse(null);

        if (menuIteratorType().equals(MenuIteratorType.HORIZONTAL)) {
            while (highestPos != null && highestPos.getColumn() < 9) {
                SlotPos lastAdded = null;

                for (SlotPos pos : newSlotPositions) {
                    if (pos.getColumn() + highestPos.getColumn() + 1 >= 9) break;

                    SlotPos added = new SlotPos(pos.getRow(), pos.getColumn() + highestPos.getColumn() + 1);
                    lastAdded = added;

                    newSlotPositions.add(added);
                }

                highestPos = lastAdded;
            }
        } else if (menuIteratorType().equals(MenuIteratorType.VERTICAL)) {
            while (highestPos != null && highestPos.getRow() < 6) {
                SlotPos lastAdded = null;

                for (SlotPos pos : newSlotPositions) {
                    if (pos.getRow() + highestPos.getRow() + 1 >= 6) break;

                    SlotPos added = new SlotPos(pos.getRow() + highestPos.getRow() + 1, pos.getColumn());
                    lastAdded = added;

                    newSlotPositions.add(added);
                }

                highestPos = lastAdded;
            }
        }

        return new ArrayList<>(newSlotPositions);
    }

    @Override
    default void handle(MenuIterator menuIterator) {
    }
}
