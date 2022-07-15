package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.SlotPosition;
import org.jetbrains.annotations.NotNull;

final class ScrollableSlotPos extends SlotPosition {

    static @NotNull ScrollableSlotPos of(int height, int width, int row, int column) {
        return new ScrollableSlotPos(height, width, row, column);
    }

    static @NotNull ScrollableSlotPos of(int height, int width, int slot) {
        return new ScrollableSlotPos(height, width, slot);
    }

    private ScrollableSlotPos(int height, int width, int row, int column) {
        super(height, width, row, column);
    }

    private ScrollableSlotPos(int height, int width, int slot) {
        super(height, width, slot);
    }
}