package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.SlotPosition;
import org.jetbrains.annotations.NotNull;

final class ScrollableSlotPos extends SlotPosition {

    static @NotNull ScrollableSlotPos of(int height, int width, int row, int column) {
        return new ScrollableSlotPos(height, width, row, column);
    }

    static @NotNull ScrollableSlotPos of(ScrollableDirection direction, int height, int width, int slot) {
        return new ScrollableSlotPos(direction, height, width, slot);
    }

    static @NotNull ScrollableSlotPos of(int height, int width, int slot) {
        return of(ScrollableDirection.HORIZONTALLY, height, width, slot);
    }

    private ScrollableSlotPos(int height, int width, int row, int column) {
        super(height, width, row, column);
    }

    private ScrollableSlotPos(ScrollableDirection direction, int height, int width, int slot) {
        super(height, width, slot);

        if (direction == ScrollableDirection.VERTICALLY) {
            this.column = slot / height;
            this.row = slot - (height * this.column);
        }
    }
}