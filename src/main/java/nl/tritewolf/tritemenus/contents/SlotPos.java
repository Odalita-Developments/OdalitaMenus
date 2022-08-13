package nl.tritewolf.tritemenus.contents;

import org.jetbrains.annotations.NotNull;

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
}