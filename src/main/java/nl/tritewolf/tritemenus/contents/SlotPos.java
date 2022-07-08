package nl.tritewolf.tritemenus.contents;

import lombok.Getter;

@Getter
public final class SlotPos {

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static SlotPos of(int slot) {
        return new SlotPos(slot);
    }

    private final int row;
    private final int column;
    private final int slot;

    SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
        this.slot = (row * 9) + column;
    }

    SlotPos(int slot) {
        this.slot = slot;
        this.row = slot / 9;
        this.column = slot - (9 * this.row);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SlotPos slotPos) {
            return slotPos.getRow() == this.row && slotPos.getColumn() == this.column;
        }

        return false;
    }
}