package nl.tritewolf.tritemenus.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TriteSlotPos {

    public static TriteSlotPos of(int row, int column) {
        return new TriteSlotPos(row, column);
    }

    public static TriteSlotPos of(int slot) {
        return new TriteSlotPos(slot);
    }

    private final int row;
    private final int column;

    public TriteSlotPos(int slot) {
        if (slot >= 0 && slot <= 8) {
            this.row = 0;
            this.column = slot;
        } else if (slot >= 9 && slot <= 17) {
            this.row = 1;
            this.column = slot - 9;
        } else if (slot >= 18 && slot <= 26) {
            this.row = 2;
            this.column = slot - 18;
        } else if (slot >= 27 && slot <= 35) {
            this.row = 3;
            this.column = slot - 27;
        } else if (slot >= 36 && slot <= 44) {
            this.row = 4;
            this.column = slot - 36;
        } else if (slot >= 45 && slot <= 53) {
            this.row = 5;
            this.column = slot - 45;
        } else {
            this.row = 0;
            this.column = 0;
        }
    }

    public int getSlot() {
        return (this.row * 9) + this.column;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TriteSlotPos) {
            TriteSlotPos slotPos = (TriteSlotPos) object;
            return slotPos.getRow() == this.row && slotPos.getColumn() == this.column;
        }

        return false;
    }
}