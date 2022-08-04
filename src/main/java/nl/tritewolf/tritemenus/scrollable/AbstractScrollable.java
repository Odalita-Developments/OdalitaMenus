package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

abstract sealed class AbstractScrollable implements Scrollable permits PatternScrollable, SingleScrollable {

    protected final ScrollableBuilderImpl builder;

    protected final String id;
    protected final InventoryContents contents;
    protected final int showYAxis, showXAxis;
    protected final int startRow, startColumn;

    protected final ScrollableDirection direction;

    protected int currentYAxis = 0;
    protected int currentXAxis = 0;

    protected final NavigableMap<Integer, Supplier<MenuItem>> items = new TreeMap<>();

    protected AbstractScrollable(@NotNull ScrollableBuilderImpl builder) {
        this.builder = builder;

        this.id = builder.getId();
        this.contents = builder.getContents();
        this.showYAxis = builder.getShowYAxis();
        this.showXAxis = builder.getShowXAxis();
        this.startRow = builder.getStartRow();
        this.startColumn = builder.getStartColumn();

        this.direction = builder.getDirection();

        builder.getItems().forEach(this::addItem);
    }

    @Override
    public int currentVertical() {
        return this.currentYAxis;
    }

    @Override
    public int currentHorizontal() {
        return this.currentXAxis;
    }

    @Override
    public @NotNull Scrollable openVertical(int newYAxis) {
        return this.open(newYAxis, ScrollableDirection.VERTICALLY);
    }

    @Override
    public @NotNull Scrollable openHorizontal(int newXAxis) {
        return this.open(newXAxis, ScrollableDirection.HORIZONTALLY);
    }

    @Override
    public @NotNull Scrollable nextVertical() {
        return this.openVertical(this.currentYAxis + 1);
    }

    @Override
    public @NotNull Scrollable previousVertical() {
        return this.openVertical(this.currentYAxis - 1);
    }

    @Override
    public @NotNull Scrollable nextHorizontal() {
        return this.openHorizontal(this.currentXAxis + 1);
    }

    @Override
    public @NotNull Scrollable previousHorizontal() {
        return this.openHorizontal(this.currentXAxis - 1);
    }

    @Override
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

    @Override
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

    abstract @NotNull Scrollable open(int newAxis, @NotNull ScrollableDirection direction);

    protected void cleanMenuGrid() {
        for (int row = this.startRow; row < this.showYAxis; row++) {
            for (int column = this.startColumn; column < this.showXAxis; column++) {
                this.contents.getTriteMenu().getContents()[row][column] = null;
            }
        }
    }

    protected void updateItem(int slot, Supplier<MenuItem> menuItemSupplier) {
        MenuObject menuObject = this.contents.getTriteMenu();

        if (menuItemSupplier == null) {
            InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
            return;
        }

        MenuItem menuItem = menuItemSupplier.get();
        if (menuItem.isUpdatable()) {
            menuObject.setHasUpdatableItems(true);
        }

        SlotPos slotPos = SlotPos.of(slot);
        menuObject.getContents()[slotPos.getRow()][slotPos.getColumn()] = menuItem;

        InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItem.getItemStack(), menuObject.getInventory());
    }

    protected boolean isValidDirection(@NotNull ScrollableDirection direction) {
        if (this.direction != ScrollableDirection.ALL && this.direction != direction) {
            throw new IllegalArgumentException("Cannot open a single scrollable in a different direction.");
        }

        return true;
    }

    protected boolean isInsideMenu(int index) {
        return index < this.showYAxis * this.showXAxis;
    }

    protected AbstractScrollable setNewPage(int newAxis, ScrollableDirection direction) {
        if (direction == ScrollableDirection.HORIZONTALLY) {
            this.currentXAxis = newAxis;
        } else {
            this.currentYAxis = newAxis;
        }

        return this;
    }
}