package nl.tritewolf.tritemenus.iterators;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TriteIterator {

    private final TriteInventoryContents inventoryContents;
    private final Set<Integer> blacklist = new HashSet<>();

    private TriteIteratorType iteratorType;
    private int row;
    private int column;

    private boolean started = false;

    public void set(TriteMenuItem menuItem) {
        System.out.println(row + " - " + column);
        inventoryContents.set(TriteSlotPos.of(row, column), menuItem);
    }

    public TriteIterator previous() {
        if (this.row == 0 && this.column == 0) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            }
            switch (iteratorType) {
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
        while (!canSet(TriteSlotPos.of(row, column)) && (row != 0 || column != 0));
        return this;
    }

    public TriteIterator next() {
        if (ended()) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            }

            switch (iteratorType) {
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
        while (!canSet(TriteSlotPos.of(row, column)) && !ended());
        return this;
    }


    public void blacklist(int slot) {
        this.blacklist.add(slot);
    }

    public void blacklist(int... slots) {
        Arrays.stream(slots).forEach(blacklist::add);
    }

    public boolean ended() {
        return row == inventoryContents.getTriteMenu().getRows() - 1 && column == 9 - 1;
    }

    public boolean canSet(TriteSlotPos slot) {
        TriteMenuObject triteMenu = inventoryContents.getTriteMenu();
        TriteMenuItem content = triteMenu.getContent(slot);
        return !blacklist.contains(slot.getSlot()) && content == null;
    }
}
