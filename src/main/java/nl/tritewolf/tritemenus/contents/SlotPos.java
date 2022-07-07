package nl.tritewolf.tritemenus.contents;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class SlotPos {

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static SlotPos of(int slot) {
        return new SlotPos(slot);
    }

    private final int row;
    private final int column;

    public SlotPos(int slot) {
        this.row = slot / 9;
        this.column = slot - (9 * this.row);
    }

    public int getSlot() {
        return (this.row * 9) + this.column;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SlotPos) {
            SlotPos slotPos = (SlotPos) object;
            return slotPos.getRow() == this.row && slotPos.getColumn() == this.column;
        }

        return false;
    }
}