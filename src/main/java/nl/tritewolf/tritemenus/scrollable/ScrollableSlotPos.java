package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.contents.pos.SlotPosition;
import org.jetbrains.annotations.NotNull;

class ScrollableSlotPos extends SlotPosition {

    static @NotNull ScrollableSlotPos of(int height, int width, int row, int column) {
        return new ScrollableSlotPos(height, width, row, column);
    }

    static @NotNull ScrollableSlotPos of(@NotNull ScrollableDirection direction, int height, int width, int slot) {
        return new ScrollableSlotPos(direction, height, width, slot);
    }

    static @NotNull ScrollableSlotPos of(int height, int width, int slot) {
        return new ScrollableSlotPos(ScrollableDirection.HORIZONTALLY, height, width, slot);
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

    static class SingleScrollableSlotPos extends ScrollableSlotPos {

        static @NotNull SingleScrollableSlotPos of(@NotNull ScrollableDirection direction, int height, int width, int slot) {
            return new SingleScrollableSlotPos(direction, height, width, slot);
        }

        private SingleScrollableSlotPos(ScrollableDirection direction, int height, int width, int slot) {
            super(direction, height, width, slot);

            if (direction == ScrollableDirection.HORIZONTALLY) {
                this.row = slot % height;
                this.column = slot / height;
            } else if (direction == ScrollableDirection.VERTICALLY) {
                this.row = slot / width;
                this.column = slot % width;
            }

            this.slot = SlotPos.of(this.row, this.column).getSlot();
        }
    }
}