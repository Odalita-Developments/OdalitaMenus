package nl.tritewolf.tritemenus.contents.pos;

import nl.tritewolf.tritemenus.contents.MenuFrameData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SlotPos extends SlotPosition {

    public static @NotNull SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static @NotNull SlotPos of(int slot) {
        return new SlotPos(slot);
    }

    public static @NotNull SlotPos of(int height, int width, int row, int column) {
        return new SlotPos(height, width, row, column);
    }

    public static @NotNull SlotPos of(int height, int width, int slot) {
        return new SlotPos(height, width, slot);
    }

    private SlotPos(int height, int width, int row, int column) {
        super(height, width, row, column);
    }

    private SlotPos(int row, int column) {
        this(6, 9, row, column);
    }

    private SlotPos(int height, int width, int slot) {
        super(height, width, slot);
    }

    private SlotPos(int slot) {
        this(6, 9, slot);
    }

    public @NotNull SlotPos convertTo(int height, int width) {
        if (this.createdFromSlot) {
            return new SlotPos(height, width, this.slot);
        }

        return new SlotPos(height, width, this.row, this.column);
    }

    public @NotNull SlotPos convertFromFrame(int height, int width, @Nullable MenuFrameData frameData) {
        if (frameData == null) return this;

        int startRow = frameData.startRow();
        int startColumn = frameData.startColumn();

        int startSlot = startRow * width + startColumn;
        int slot = startSlot + (this.row * width) + this.column;
        return new SlotPos(height, width, slot);
    }
}