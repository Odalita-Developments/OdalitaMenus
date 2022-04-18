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
        triteMenu.setHasUpdatableItems(triteMenuItem.isUpdatable());
        triteMenu.getContents().put(slotPos, triteMenuItem);

        if (triteMenuItem instanceof TriteSearchItem) {
            TriteSearchItem triteSearchItem = (TriteSearchItem) triteMenuItem;
            triteMenu.getSearchQueries().put(triteSearchItem.getId(), null);
        }
    }

    public void fillRow(int row, TriteMenuItem item) {
        if (row >= triteMenu.getContents().size())
            return;

        for (int column = 0; column < 9; column++) {
            addItem(new TriteSlotPos(row, column), item);
        }
    }

    public void fillColumn(int column, TriteMenuItem item) {
        for (int row = 0; row < triteMenu.getRows(); row++) {
            addItem(new TriteSlotPos(row, column), item);
        }
    }

    public void fillBorders(TriteMenuItem item) {
        fillRect(0, 0, triteMenu.getRows() - 1, triteMenu.getRows() - 1, item);
    }

    public void fillRect(int fromRow, int fromColumn, int toRow, int toColumn, TriteMenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn)
                    continue;

                addItem(new TriteSlotPos(row, column), item);
            }
        }
    }

    public String getSearchQuery(String id) {
        return triteMenu.getSearchQueries().get(id);
    }
}
