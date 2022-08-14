package nl.tritewolf.tritemenus.contents.pos;

import lombok.Getter;

@Getter
public abstract class SlotPosition {

    protected final int height, width;

    protected int row, column;
    protected int slot;

    protected SlotPosition(int height, int width, int row, int column) {
        this.height = height;
        this.width = width;
        this.row = row;
        this.column = column;
        this.slot = (row * this.width) + column;
    }

    protected SlotPosition(int height, int width, int slot) {
        this.height = height;
        this.width = width;
        this.slot = slot;
        this.row = slot / this.width;
        this.column = slot - (this.width * this.row);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SlotPosition slotPosition) {
            return slotPosition.getHeight() == this.height && slotPosition.getWidth() == this.width
                    && slotPosition.getRow() == this.row && slotPosition.getColumn() == this.column;
        }

        return false;
    }
}