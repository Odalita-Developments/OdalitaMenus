package nl.tritewolf.tritemenus.contents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.items.buttons.TriteSearchItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;

@Getter
@RequiredArgsConstructor
public class TriteInventoryContents {

    private final TriteMenuObject triteMenu;

    public void addItem(TriteSlotPos slotPos, TriteMenuItem triteMenuItem) {
        this.triteMenu.setHasUpdatableItems(triteMenuItem.isUpdatable());
        this.triteMenu.getContents().put(slotPos, triteMenuItem);

        if (triteMenuItem instanceof TriteSearchItem) {
            TriteSearchItem triteSearchItem = (TriteSearchItem) triteMenuItem;
            this.triteMenu.getSearchQueries().put(triteSearchItem.getId(), null);
        }
    }

    public void fillRow(int row, TriteMenuItem item) {
        if (row >= this.triteMenu.getContents().size())
            return;

        for (int column = 0; column < 9; column++) {
            this.addItem(new TriteSlotPos(row, column), item);
        }
    }

    public void fillColumn(int column, TriteMenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            this.addItem(new TriteSlotPos(row, column), item);
        }
    }

    public void fillBorders(TriteMenuItem item) {
        fillRectangle(0, 0, this.triteMenu.getRows() - 1, 8, item);
    }

    public void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, TriteMenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn)
                    continue;

                this.addItem(new TriteSlotPos(row, column), item);
            }
        }
    }

    public void fill(TriteMenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                TriteSlotPos slotPos = new TriteSlotPos(row, column);
                if (this.triteMenu.getContents().containsKey(slotPos)) continue;

                this.addItem(slotPos, item);
            }
        }
    }

    public String getSearchQuery(String id) {
        return this.triteMenu.getSearchQueries().get(id);
    }
}
