package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

final class SingleScrollable extends AbstractScrollable {

    private int lastRow, lastColumn;

    SingleScrollable(@NotNull ScrollableBuilderImpl builder) {
        super(builder);

        this.lastRow = this.startRow;
        this.lastColumn = this.startColumn;
    }

    @Override
    public synchronized @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItemSupplier) {
        int index = (this.items.isEmpty()) ? 0 : this.items.lastKey() + 1;

        this.items.put(index, menuItemSupplier);

        if (this.isInsideMenu(index)) {
            this.contents.setAsync(SlotPos.of(this.lastRow, this.lastColumn), menuItemSupplier.get());

            if (this.direction == ScrollableDirection.HORIZONTALLY) {
                this.lastRow++;

                if (this.lastRow >= this.showYAxis + this.startRow && this.lastColumn + 1 < this.showXAxis + this.startColumn) {
                    this.lastRow = this.startRow;
                    this.lastColumn++;
                }
            } else if (this.direction == ScrollableDirection.VERTICALLY) {
                this.lastColumn++;

                if (this.lastColumn >= this.showXAxis + this.startColumn && this.lastRow + 1 < this.showYAxis + this.startRow) {
                    this.lastColumn = this.startColumn;
                    this.lastRow++;
                }
            }
        }

        return this;
    }

    @Override
    public int lastVertical() {
        return Math.max(0, (int) Math.ceil((double) this.items.size() / (double) this.showXAxis) - this.showYAxis);
    }

    @Override
    public int lastHorizontal() {
        return Math.max(0, (int) Math.ceil((double) this.items.size() / (double) this.showYAxis) - this.showXAxis);
    }

    @Override
    @NotNull Scrollable open(int newAxis, @NotNull ScrollableDirection direction) {
        if (!this.isValidDirection(direction)) return this;

        int lastAxis = (direction == ScrollableDirection.HORIZONTALLY) ? this.lastHorizontal() : this.lastVertical();
        if (newAxis > lastAxis) return this;

        newAxis = Math.max(0, newAxis);

        List<Supplier<MenuItem>> pageItems = this.getPageItems(newAxis, direction);
        if (pageItems.isEmpty()) return this;

        boolean updatable = this.contents.getTriteMenu().isHasUpdatableItems();
        if (updatable) {
            this.contents.getTriteMenu().setHasUpdatableItems(false);
        }

        int lastRow = this.startRow;
        int lastColumn = this.startColumn;
        for (Supplier<MenuItem> menuItemSupplier : pageItems) {
            if (updatable) {
                MenuItem menuItem = this.contents.getTriteMenu().getContents()[lastRow][lastColumn];
                if (menuItem != null && menuItem.isUpdatable()) {
                    this.contents.getTriteMenu().getContents()[lastRow][lastColumn] = null;
                }
            }

            this.updateItem(SlotPos.of(lastRow, lastColumn).getSlot(), menuItemSupplier);

            if (direction == ScrollableDirection.HORIZONTALLY) {
                lastRow++;

                if (lastRow >= this.showYAxis + this.startRow && lastColumn + 1 < this.showXAxis + this.startColumn) {
                    lastRow = this.startRow;
                    lastColumn++;
                }
            } else if (direction == ScrollableDirection.VERTICALLY) {
                lastColumn++;

                if (lastColumn >= this.showXAxis + this.startColumn && lastRow + 1 < this.showYAxis + this.startRow) {
                    lastColumn = this.startColumn;
                    lastRow++;
                }
            }
        }

        return this.setNewPage(newAxis, direction);
    }

    private List<Supplier<MenuItem>> getPageItems(int newAxis, ScrollableDirection direction) {
        int startIndex = (direction == ScrollableDirection.HORIZONTALLY)
                ? newAxis * this.showYAxis
                : newAxis * this.showXAxis;

        int endIndex = (direction == ScrollableDirection.HORIZONTALLY)
                ? (newAxis + this.showXAxis) * this.showYAxis
                : (newAxis + this.showYAxis) * this.showXAxis;

        List<Supplier<MenuItem>> pageItems = new ArrayList<>(
                this.items.subMap(startIndex, endIndex).values()
        );

        for (int i = pageItems.size(); i < this.showXAxis * this.showYAxis; i++) {
            pageItems.add(null);
        }

        return pageItems;
    }
}