package nl.odalitadevelopments.menus.scrollable;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.utils.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

abstract sealed class AbstractScrollable implements Scrollable permits SingleScrollable, PatternScrollable {

    protected final ScrollableBuilderImpl builder;

    protected final String id;
    protected final MenuContents contents;
    protected final int showYAxis, showXAxis;
    protected final int startRow, startColumn;

    protected final ScrollableDirection direction;

    protected int currentYAxis = 0;
    protected int currentXAxis = 0;

    protected final NavigableMap<Integer, Supplier<MenuItem>> items = new TreeMap<>();

    private boolean initialized = false;

    protected AbstractScrollable(@NotNull ScrollableBuilderImpl builder) {
        this.builder = builder;

        this.id = builder.getId();
        this.contents = builder.getContents();
        this.showYAxis = builder.getShowYAxis();
        this.showXAxis = builder.getShowXAxis();
        this.startRow = builder.getStartRow();
        this.startColumn = builder.getStartColumn();

        this.direction = builder.getDirection();
    }

    protected abstract int newItemIndex();

    protected abstract @NotNull Pair<Integer, Integer> getStartEndIndexes();

    protected abstract int getIndexOffset(int index, int axis, @NotNull ScrollableDirection direction);

    protected abstract @NotNull ScrollableSlotPos createSlotPos(int index);

    protected @NotNull Pair<@NotNull Integer, @NotNull Integer> rowColumnModifier(int row, int column) {
        return new Pair<>(row, column);
    }

    @Override
    public final synchronized @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItemSupplier) {
        int index = this.newItemIndex();
        if (index < 0) return this;

        this.items.put(index, menuItemSupplier);

        if (this.initialized) {
            this.calculateSlotPosWithoutOffset(index).ifPresent((slotPos) -> {
                this.contents.setAsync(SlotPos.of(slotPos), menuItemSupplier.get());
            });
        }

        return this;
    }

    @Override
    public final int currentVertical() {
        return this.currentYAxis;
    }

    @Override
    public final int currentHorizontal() {
        return this.currentXAxis;
    }

    @Override
    public final @NotNull Scrollable openVertical(int newYAxis) {
        return this.open(newYAxis, ScrollableDirection.VERTICALLY);
    }

    @Override
    public final @NotNull Scrollable openHorizontal(int newXAxis) {
        return this.open(newXAxis, ScrollableDirection.HORIZONTALLY);
    }

    @Override
    public final @NotNull Scrollable nextVertical() {
        return this.openVertical(this.currentYAxis + 1);
    }

    @Override
    public final @NotNull Scrollable previousVertical() {
        return this.openVertical(this.currentYAxis - 1);
    }

    @Override
    public final @NotNull Scrollable nextHorizontal() {
        return this.openHorizontal(this.currentXAxis + 1);
    }

    @Override
    public final @NotNull Scrollable previousHorizontal() {
        return this.openHorizontal(this.currentXAxis - 1);
    }

    @Override
    public final @NotNull Scrollable next() {
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
    public final @NotNull Scrollable previous() {
        return switch (this.direction) {
            case VERTICALLY:
                yield this.previousVertical();
            case HORIZONTALLY:
                yield this.previousHorizontal();
            case ALL:
                throw new UnsupportedOperationException("Previous is not supported for 'all' direction.");
        };
    }

    @ApiStatus.Internal
    @Override
    public final void setAxes(int xAxis, int yAxis) {
        if (this.initialized) return;

        this.currentXAxis = Math.max(0, Math.min(this.lastHorizontal(), xAxis));
        this.currentYAxis = Math.max(0, Math.min(this.lastVertical(), yAxis));
    }

    @ApiStatus.Internal
    @Override
    public final void setInitialized(boolean initialized) {
        if (this.initialized) return;

        this.initialized = initialized;
    }

    @ApiStatus.Internal
    @Override
    public final @NotNull Map<Integer, Supplier<MenuItem>> getPageItems() {
        if (this.initialized) return Map.of();

        Map<Integer, Supplier<MenuItem>> itemsBySlot = new HashMap<>();
        Map<Integer, Supplier<MenuItem>> itemsByIndex = this.getItems();

        int axis;
        if (this.direction == ScrollableDirection.HORIZONTALLY) {
            axis = this.currentXAxis;
        } else {
            axis = this.currentYAxis;
        }

        for (Map.Entry<Integer, Supplier<MenuItem>> entry : itemsByIndex.entrySet()) {
            this.calculateSlotPosWithOffset(entry.getKey(), axis, this.direction).ifPresent((slot) -> {
                itemsBySlot.put(slot, entry.getValue());
            });
        }

        return itemsBySlot;
    }

    protected final @NotNull Optional<@NotNull Integer> calculateSlotPosWithoutOffset(int index) {
        return this.calculateSlotPos(index, 0, this.direction, false);
    }

    protected final @NotNull Optional<@NotNull Integer> calculateSlotPosWithOffset(int index, int axis, @NotNull ScrollableDirection direction) {
        return this.calculateSlotPos(index, axis, direction, true);
    }

    private @NotNull Scrollable open(int newAxis, @NotNull ScrollableDirection direction) {
        if (this.direction != ScrollableDirection.ALL && this.direction != direction) {
            throw new IllegalArgumentException("Cannot open a scrollable in the wrong direction.");
        }

        int lastAxis = (direction == ScrollableDirection.HORIZONTALLY) ? this.lastHorizontal() : this.lastVertical();
        if (newAxis < 0 || newAxis > lastAxis) return this;

        int oldAxis = this.updateCurrentAxis(newAxis, direction);

        Map<Integer, Supplier<MenuItem>> pageItems = this.getItems();
        if (pageItems.isEmpty()) {
            this.updateCurrentAxis(oldAxis, direction);
            return this;
        }

        this.cleanMenuGrid();

        for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
            this.calculateSlotPosWithOffset(entry.getKey(), newAxis, direction).ifPresent((slot) -> {
                this.updateItem(slot, entry.getValue());
            });
        }

        this.contents.menuSession().getCache().getPageSwitchUpdateItems().forEach((slot, item) -> {
            this.contents.setAsync(slot, item.get());
        });

        return this;
    }

    private Map<Integer, Supplier<MenuItem>> getItems() {
        Pair<Integer, Integer> startEndIndexes = this.getStartEndIndexes();

        int startIndex = startEndIndexes.getKey();
        int endIndex = startEndIndexes.getValue();

        Map<Integer, Supplier<MenuItem>> items = new HashMap<>(
                this.items.subMap(startIndex, endIndex)
        );

        for (int i = startIndex; i < endIndex; i++) {
            items.putIfAbsent(i, null);
        }

        return items;
    }

    private @NotNull Optional<@NotNull Integer> calculateSlotPos(int index, int axis, @NotNull ScrollableDirection direction, boolean offsetPresent) {
        int offset = (offsetPresent) ? this.getIndexOffset(index, axis, direction) : 0;

        ScrollableSlotPos slotPos = this.createSlotPos(index - offset);
        Pair<Integer, Integer> rowColumnModifier = this.rowColumnModifier(slotPos.getRow(), slotPos.getColumn());

        int row = this.startRow + rowColumnModifier.getKey();
        int column = this.startColumn + rowColumnModifier.getValue();

        if (row < this.startRow || column < this.startColumn || row >= this.startRow + this.showYAxis || column >= this.startColumn + this.showXAxis) {
            return Optional.empty();
        }

        return Optional.of(SlotPos.of(row, column).getSlot());
    }

    private void cleanMenuGrid() {
        for (int row = this.startRow; row < this.startRow + this.showYAxis; row++) {
            for (int column = this.startColumn; column < this.startColumn + this.showXAxis; column++) {
                MenuItem menuItem = this.contents.menuSession().getContents()[row][column];
                if (this.contents.menuSession().isHasUpdatableItems() && menuItem != null && menuItem.isUpdatable()) {
                    this.contents.menuSession().setHasUpdatableItems(false);
                }

                this.contents.menuSession().getContents()[row][column] = null;
            }
        }
    }

    private void updateItem(int slot, Supplier<MenuItem> menuItemSupplier) {
        MenuItem menuItem = (menuItemSupplier == null) ? DisplayItem.of(new ItemStack(Material.AIR)) : menuItemSupplier.get();
        this.contents.setAsync(slot, menuItem);
    }

    private int updateCurrentAxis(int newAxis, ScrollableDirection direction) {
        int oldAxis;

        if (direction == ScrollableDirection.VERTICALLY) {
            oldAxis = this.currentYAxis;
            this.currentYAxis = newAxis;
        } else {
            oldAxis = this.currentXAxis;
            this.currentXAxis = newAxis;
        }

        return oldAxis;
    }

    final void initItems() {
        this.builder.getItems().forEach(this::addItem);
    }
}