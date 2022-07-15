package nl.tritewolf.tritemenus.contents;

public final class SlotPos extends SlotPosition {

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static SlotPos of(int slot) {
        return new SlotPos(slot);
    }

    private SlotPos(int row, int column) {
        super(6, 9, row, column);
    }

    private SlotPos(int slot) {
        super(6, 9, slot);
    }
}