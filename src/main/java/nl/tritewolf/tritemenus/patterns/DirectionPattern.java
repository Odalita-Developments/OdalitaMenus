package nl.tritewolf.tritemenus.patterns;

import com.google.common.collect.Sets;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.utils.CharacterUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface DirectionPattern extends MenuPattern<List<SlotPos>> {

    boolean shouldContinuePattern();

    MenuIteratorType menuIteratorType();

    default int startingRow() {
        return 0;
    }

    default int startingColumn() {
        return 0;
    }

    @Override
    default @NotNull List<SlotPos> getCache() {
        TreeMap<Integer, SlotPos> slots = new TreeMap<>();
        int lastIndex = 0;
        for (int row = 0; row < getPattern().size(); row++) {
            for (int column = 0; column < getPattern().get(row).length(); column++) {
                if (lastIndex >= getPattern().get(row).length()) continue;

                String string = getPattern().get(row).substring(lastIndex, lastIndex += 2);
                if (string.equalsIgnoreCase("##")) continue;
                if (CharacterUtils.isDigits(string)) {
                    slots.put(Integer.parseInt(string), SlotPos.of((row + startingRow()), (column + startingColumn())));
                }
            }
            lastIndex = 0;
        }

        Collection<SlotPos> slotsNew = Sets.newCopyOnWriteArraySet(slots.values());
        if (shouldContinuePattern()) {
            if (menuIteratorType().equals(MenuIteratorType.HORIZONTAL)) {
                SlotPos slotPos = slotsNew.stream().max(Comparator.comparing(SlotPos::getColumn)).orElse(null);
                while (slotPos != null && slotPos.getColumn() < 9) {
                    SlotPos lastAdded = null;
                    for (SlotPos pos : slotsNew) {
                        if (pos.getColumn() + slotPos.getColumn() + 1 >= 9) break;

                        SlotPos added = SlotPos.of(pos.getRow(), pos.getColumn() + slotPos.getColumn() + 1);
                        lastAdded = added;

                        slotsNew.add(added);
                    }
                    slotPos = lastAdded;
                }
            } else if (menuIteratorType().equals(MenuIteratorType.VERTICAL)) {
                SlotPos slotPos = slotsNew.stream().max(Comparator.comparing(SlotPos::getRow)).orElse(null);
                while (slotPos != null && slotPos.getRow() < 7) {
                    SlotPos lastAdded = null;
                    for (SlotPos pos : slotsNew) {
                        if (pos.getRow() + slotPos.getRow() + 1 >= 6) break;

                        SlotPos added = SlotPos.of(pos.getRow() + slotPos.getRow() + 1, pos.getColumn());
                        lastAdded = added;

                        slotsNew.add(added);
                    }
                    slotPos = lastAdded;
                }
            }
        }
        return new ArrayList<>(slotsNew);
    }

    @Override
    default void handle(@NotNull MenuIterator menuIterator) {
    }
}
