package nl.odalitadevelopments.menus.contents.pos;

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public abstract class SlotPosition {

    protected final int height, width;

    protected int row, column;
    protected int slot;

    protected boolean createdFromSlot;

    protected SlotPosition(int height, int width, int row, int column) {
        Preconditions.checkArgument(height >= 0, "height must be greater than 0");
        Preconditions.checkArgument(width >= 0, "width must be greater than 0");
        Preconditions.checkArgument(row >= 0, "row must be greater than 0");
        Preconditions.checkArgument(column >= 0, "column must be greater than 0");

        this.height = height;
        this.width = width;
        this.row = row;
        this.column = column;
        this.slot = (row * this.width) + column;
        this.createdFromSlot = false;
    }

    protected SlotPosition(int height, int width, int slot) {
        Preconditions.checkArgument(height >= 0, "height must be greater than 0");
        Preconditions.checkArgument(width >= 0, "width must be greater than 0");
        Preconditions.checkArgument(slot >= 0, "slot must be greater than 0");

        this.height = height;
        this.width = width;
        this.slot = slot;
        this.row = slot / this.width;
        this.column = slot - (this.width * this.row);
        this.createdFromSlot = true;
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