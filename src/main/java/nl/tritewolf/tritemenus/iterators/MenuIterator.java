package nl.tritewolf.tritemenus.iterators;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public final class MenuIterator {

    private final InventoryContents inventoryContents;
    private final Set<Integer> blacklist = new HashSet<>();
    private final boolean override;

    private MenuIteratorType menuIteratorType;

    private final int startRow;
    private final int startColumn;

    private int row;
    private int column;

    private boolean started = false;

    public MenuIterator(InventoryContents inventoryContents, boolean override, MenuIteratorType menuIteratorType, int startRow, int startColumn, boolean started) {
        this.inventoryContents = inventoryContents;
        this.override = override;
        this.menuIteratorType = menuIteratorType;
        this.startRow = startRow;
        this.startColumn = startColumn;

        this.row = startRow;
        this.column = startColumn;

        this.started = started;
    }

    public MenuIterator set(MenuItem menuItem) {
        this.inventoryContents.set(SlotPos.of(this.row, this.column), menuItem);
        return this;
    }

    public synchronized MenuIterator setAsync(MenuItem menuItem) {
        this.inventoryContents.setAsync(SlotPos.of(this.row, this.column), menuItem);
        return this;
    }

    public MenuIterator previous() {
        if (this.row == 0 && this.column == 0) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            }
            switch (this.menuIteratorType) {
                case VERTICAL:
                    this.column--;

                    if (this.column == 0) {
                        this.column = 9 - 1;
                        this.row--;
                    }
                    break;
                case HORIZONTAL:
                    this.row--;

                    if (this.row == 0) {
                        this.row = this.inventoryContents.getTriteMenu().getRows() - 1;
                        this.column--;
                    }
                    break;

            }
        }
        while (!canSet(SlotPos.of(this.row, this.column)) && (this.row != 0 || this.column != 0));
        return this;
    }

    public MenuIterator next() {
        if (ended()) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            }

            switch (this.menuIteratorType) {
                case HORIZONTAL:
                    this.column = ++this.column % 9;

                    if (this.column == 0) row++;
                    break;
                case VERTICAL:
                    this.row = ++this.row % this.inventoryContents.getTriteMenu().getRows();

                    if (this.row == 0) this.column++;
                    break;

            }
        }
        while (!canSet(SlotPos.of(this.row, this.column)) && !ended());
        return this;
    }

    public MenuIterator blacklist(int slot) {
        this.blacklist.add(slot);
        return this;
    }

    public MenuIterator blacklist(int... slots) {
        Arrays.stream(slots).forEach(this.blacklist::add);
        return this;
    }

    public boolean ended() {
        return this.row == this.inventoryContents.getTriteMenu().getRows() - 1 && this.column == 9 - 1;
    }

    public boolean canSet(SlotPos slot) {
        if (this.override) {
            return !this.blacklist.contains(slot.getSlot());
        }

        MenuObject triteMenu = this.inventoryContents.getTriteMenu();
        MenuItem content = triteMenu.getContent(slot);
        return !this.blacklist.contains(slot.getSlot()) && content == null;
    }

    public MenuIterator reset() {
        this.row = this.startRow;
        this.column = this.startColumn;
        return this;
    }

    public SlotPos getSlotPos() {
        return SlotPos.of(this.row, this.column);
    }
}