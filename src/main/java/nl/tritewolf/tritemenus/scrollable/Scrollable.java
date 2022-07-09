package nl.tritewolf.tritemenus.scrollable;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.scrollable.pattern.DirectionScrollablePattern;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

@Getter
public final class Scrollable {

    private final String id;
    private final InventoryContents contents;
    private final int showYAxis, showXAxis;
    private final int startRow, startColumn;
    private final boolean isSingle;

    private final ScrollableBuilder.PatternDirection direction;
    private final DirectionScrollablePattern pattern;

    private final NavigableMap<Integer, Supplier<MenuItem>> items = new TreeMap<>();

    @Setter
    private int currentYAxis = 0;
    @Setter
    private int currentXAxis = 0;

    private int lastRow, lastColumn;

    Scrollable(@NotNull ScrollableBuilder builder) {
        this.id = builder.getId();
        this.contents = builder.getContents();
        this.showYAxis = builder.getShowYAxis();
        this.showXAxis = builder.getShowXAxis();
        this.startRow = builder.getStartRow();
        this.startColumn = builder.getStartColumn();
        this.isSingle = builder.isSingle();

        this.direction = builder.getDirection();
        this.pattern = builder.getPattern();

        this.lastRow = this.startRow;
        this.lastColumn = this.startColumn;

        if (!builder.isSingle()) {
            // TODO initialize pattern
        }

        builder.getItems().forEach(this::addItem);
    }

    public synchronized @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItem) {
        int index;
        if (this.isSingle) {
            index = (this.items.isEmpty()) ? 0 : this.items.lastKey() + 1;
        } else {
            index = 0;
        }

        this.items.put(index, menuItem);

        if (index < this.showXAxis * this.showYAxis) {
            this.contents.setAsync(SlotPos.of(this.lastRow, this.lastColumn), menuItem.get());

            if (this.direction == ScrollableBuilder.PatternDirection.HORIZONTALLY) {
                this.lastRow++;

                if (this.lastRow >= this.showYAxis + this.startRow && this.lastColumn + 1 < this.showXAxis + this.startColumn) {
                    this.lastRow = this.startRow;
                    this.lastColumn++;
                }
            } else if (this.direction == ScrollableBuilder.PatternDirection.VERTICALLY) {
                this.lastColumn++;

                if (this.lastColumn >= this.showXAxis + this.startColumn && this.lastRow + 1 < this.showYAxis + this.startRow) {
                    this.lastColumn = this.startColumn;
                    this.lastRow++;
                }
            }
        }

        return this;
    }

    public int lastVertical() {
        return (int) Math.ceil((double) this.items.size() / (double) this.showXAxis) - this.showYAxis;
    }

    public int lastHorizontal() {
        return (int) Math.ceil((double) this.items.size() / (double) this.showYAxis) - this.showXAxis;
    }

    public @NotNull Scrollable nextVertical() {
        return this.openVertical(this.currentYAxis + 1);
    }

    public @NotNull Scrollable previousVertical() {
        return this.openVertical(this.currentYAxis - 1);
    }

    public @NotNull Scrollable nextHorizontal() {
        return this.openHorizontal(this.currentXAxis + 1);
    }

    public @NotNull Scrollable previousHorizontal() {
        return this.openHorizontal(this.currentXAxis - 1);
    }

    public @NotNull Scrollable next() {
        return switch (this.direction) {
            case VERTICALLY:
                yield this.nextVertical();
            case HORIZONTALLY:
                yield this.nextHorizontal();
            case ALL:
                throw new UnsupportedOperationException("Next is not supported for 'all' direction.");
        };
    }

    public @NotNull Scrollable previous() {
        return switch (this.direction) {
            case VERTICALLY:
                yield this.previousVertical();
            case HORIZONTALLY:
                yield this.previousHorizontal();
            case ALL:
                throw new UnsupportedOperationException("Previous is not supported for 'all' direction.");
        };
    }

    public @NotNull Scrollable open(int newAxis, @NotNull ScrollableBuilder.SingleDirection direction) {
        int lastAxis = (direction == ScrollableBuilder.SingleDirection.HORIZONTALLY) ? this.lastHorizontal() : this.lastVertical();
        newAxis = Math.max(0, Math.min(newAxis, lastAxis));

        List<Supplier<MenuItem>> pageItems;
        if (direction == ScrollableBuilder.SingleDirection.HORIZONTALLY) {
            pageItems = new ArrayList<>(this.items.subMap(newAxis * this.showYAxis, (newAxis + this.showXAxis) * this.showYAxis).values());
        } else {
            pageItems = new ArrayList<>(this.items.subMap(newAxis * this.showXAxis, (newAxis + this.showYAxis) * this.showXAxis).values());
        }

        for (int i = pageItems.size(); i < this.showXAxis * this.showYAxis; i++) {
            pageItems.add(null);
        }

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

            if (direction == ScrollableBuilder.SingleDirection.HORIZONTALLY) {
                lastRow++;

                if (lastRow >= this.showYAxis + this.startRow && lastColumn + 1 < this.showXAxis + this.startColumn) {
                    lastRow = this.startRow;
                    lastColumn++;
                }
            } else if (direction == ScrollableBuilder.SingleDirection.VERTICALLY) {
                lastColumn++;

                if (lastColumn >= this.showXAxis + this.startColumn && lastRow + 1 < this.showYAxis + this.startRow) {
                    lastColumn = this.startColumn;
                    lastRow++;
                }
            }
        }

        if (direction == ScrollableBuilder.SingleDirection.HORIZONTALLY) {
            this.currentXAxis = newAxis;
        } else {
            this.currentYAxis = newAxis;
        }

        return this;
    }

    public @NotNull Scrollable openVertical(int newYAxis) {
        return this.open(newYAxis, ScrollableBuilder.SingleDirection.VERTICALLY);
    }

    public @NotNull Scrollable openHorizontal(int newXAxis) {
        return this.open(newXAxis, ScrollableBuilder.SingleDirection.HORIZONTALLY);
    }

    private void updateItem(int slot, Supplier<MenuItem> menuItemSupplier) {
        MenuObject menuObject = this.contents.getTriteMenu();

        if (menuItemSupplier == null) {
            InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
            return;
        }

        MenuItem menuItem = menuItemSupplier.get();
        if (menuItem.isUpdatable()) {
            SlotPos slotPos = SlotPos.of(slot);

            menuObject.getContents()[slotPos.getRow()][slotPos.getColumn()] = menuItem;
            menuObject.setHasUpdatableItems(true);
        }

        InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItem.getItemStack(), menuObject.getInventory());
    }
}