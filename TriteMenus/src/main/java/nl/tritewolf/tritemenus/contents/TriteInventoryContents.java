package nl.tritewolf.tritemenus.contents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.items.buttons.TriteSearchItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class TriteInventoryContents {

    protected final TriteMenuObject triteMenu; // Make protected to create the option to extend this class and add custom methods

    public void set(TriteSlotPos slotPos, TriteMenuItem item) {
        this.triteMenu.setHasUpdatableItems(item.isUpdatable());
        this.triteMenu.getContents().put(slotPos, item);

        if (item instanceof TriteSearchItem) {
            TriteSearchItem triteSearchItem = (TriteSearchItem) item;
            this.triteMenu.getSearchQueries().put(triteSearchItem.getId(), null);
        }
    }

    public void set(int row, int column, TriteMenuItem item) {
        this.set(TriteSlotPos.of(row, column), item);
    }

    public void set(int slot, TriteMenuItem item) {
        this.set(TriteSlotPos.of(slot), item);
    }

    public void add(TriteMenuItem item) {
        this.firstEmptyPosition().ifPresent((slotPos) -> {
            this.set(slotPos, item);
        });
    }

    public Optional<TriteSlotPos> firstEmptyPosition() {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                TriteSlotPos slotPos = TriteSlotPos.of(row, column);

                if (!this.triteMenu.getContents().containsKey(slotPos)) {
                    return Optional.of(slotPos);
                }
            }
        }

        return Optional.empty();
    }

    public void fillRow(int row, TriteMenuItem item) {
        if (row >= this.triteMenu.getContents().size())
            return;

        for (int column = 0; column < 9; column++) {
            this.set(TriteSlotPos.of(row, column), item);
        }
    }

    public void fillColumn(int column, TriteMenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            this.set(TriteSlotPos.of(row, column), item);
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

                this.set(TriteSlotPos.of(row, column), item);
            }
        }
    }

    public void fillRectangle(TriteSlotPos fromPos, TriteSlotPos toPos, TriteMenuItem item) {
        this.fillRectangle(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    public void fillRectangle(int fromSlot, int toSlot, TriteMenuItem item) {
        this.fillRectangle(TriteSlotPos.of(fromSlot), TriteSlotPos.of(toSlot), item);
    }

    public void fill(TriteMenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                TriteSlotPos slotPos = TriteSlotPos.of(row, column);
                if (this.triteMenu.getContents().containsKey(slotPos)) continue;

                this.set(slotPos, item);
            }
        }
    }

    public String getSearchQuery(String id) {
        return this.triteMenu.getSearchQueries().get(id);
    }
}