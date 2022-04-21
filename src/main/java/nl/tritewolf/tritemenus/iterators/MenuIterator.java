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

    private MenuIteratorType menuIteratorType;
    private int row;
    private int column;

    private boolean started = false;

    public MenuIterator set(MenuItem menuItem) {
        inventoryContents.set(SlotPos.of(row, column), menuItem);
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
            switch (menuIteratorType) {
                case VERTICAL:
                    column--;

                    if (column == 0) {
                        column = 9 - 1;
                        row--;
                    }
                    break;
                case HORIZONTAL:
                    row--;

                    if (row == 0) {
                        row = inventoryContents.getTriteMenu().getRows() - 1;
                        column--;
                    }
                    break;

            }
        }
        while (!canSet(SlotPos.of(row, column)) && (row != 0 || column != 0));
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

            switch (menuIteratorType) {
                case HORIZONTAL:
                    column = ++column % 9;

                    if (column == 0) row++;
                    break;
                case VERTICAL:
                    row = ++row % inventoryContents.getTriteMenu().getRows();

                    if (row == 0) column++;
                    break;

            }
        }
        while (!canSet(SlotPos.of(row, column)) && !ended());
        return this;
    }


    public MenuIterator blacklist(int slot) {
        this.blacklist.add(slot);
        return this;
    }

    public MenuIterator blacklist(int... slots) {
        Arrays.stream(slots).forEach(blacklist::add);
        return this;
    }

    public boolean ended() {
        return row == inventoryContents.getTriteMenu().getRows() - 1 && column == 9 - 1;
    }

    public boolean canSet(SlotPos slot) {
        MenuObject triteMenu = inventoryContents.getTriteMenu();
        MenuItem content = triteMenu.getContent(slot);
        return !blacklist.contains(slot.getSlot()) && content == null;
    }
}