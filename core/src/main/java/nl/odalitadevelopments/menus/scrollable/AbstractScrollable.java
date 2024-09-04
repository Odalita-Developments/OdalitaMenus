package nl.odalitadevelopments.menus.scrollable;

import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.utils.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

abstract sealed class AbstractScrollable implements Scrollable permits SingleScrollableImpl, PatternScrollableImpl {

    protected final ScrollableBuilderImpl builder;

    protected final String id;
    protected final AbstractMenuSession<?, ?, ?> menuSession;
    protected final IMenuContents contents;
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
        this.menuSession = builder.getMenuSession();
        this.contents = this.menuSession.menuContents();
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
        return Pair.of(row, column);
    }

    @Override
    public final synchronized @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItemSupplier) {
        int index = this.newItemIndex();
        if (index < 0) return this;

        this.items.put(index, menuItemSupplier);

        if (this.initialized) {
            this.calculateSlotPosWithoutOffset(index).ifPresent((slotPos) -> {
                this.contents.set(SlotPos.of(slotPos), menuItemSupplier.get());
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
    public final boolean isFirstVertical() {
        return this.currentVertical() == 0;
    }

    @Override
    public final boolean isFirstHorizontal() {
        return this.currentHorizontal() == 0;
    }

    @Override
    public final boolean isLastVertical() {
        return this.currentVertical() == this.lastVertical();
    }

    @Override
    public final boolean isLastHorizontal() {
        return this.currentHorizontal() == this.lastHorizontal();
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
            case VERTICALLY -> this.nextVertical();
            case HORIZONTALLY -> this.nextHorizontal();
            case ALL -> throw new UnsupportedOperationException("Next is not supported for 'all' direction.");
        };
    }

    @Override
    public final @NotNull Scrollable previous() {
        return switch (this.direction) {
            case VERTICALLY -> this.previousVertical();
            case HORIZONTALLY -> this.previousHorizontal();
            case ALL -> throw new UnsupportedOperationException("Previous is not supported for 'all' direction.");
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
    public final void setInitialized() {
        this.initialized = true;
    }

    @ApiStatus.Internal
    @Override
    public final @NotNull Map<Integer, Supplier<MenuItem>> getPageItems() {
        if (this.initialized) return Map.of();

        Map<Integer, Supplier<MenuItem>> itemsBySlot = new HashMap<>();
        Map<Integer, Supplier<MenuItem>> itemsByIndex = this.getItems();

        int axis = (this.direction == ScrollableDirection.HORIZONTALLY) ? this.currentXAxis : this.currentYAxis;

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

        this.clearMenuGrid();

        for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
            this.calculateSlotPosWithOffset(entry.getKey(), newAxis, direction).ifPresent((slot) -> {
                this.updateItem(slot, entry.getValue());
            });
        }

        this.contents.cache().getPageSwitchUpdateItems().forEach((slot, item) -> {
            this.contents.set(slot, item.get());
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

    private void clearMenuGrid() {
        for (int row = this.startRow; row < this.startRow + this.showYAxis; row++) {
            for (int column = this.startColumn; column < this.startColumn + this.showXAxis; column++) {
                this.menuSession.contents()[row][column] = null;
            }
        }
    }

    private void updateItem(int slot, Supplier<MenuItem> menuItemSupplier) {
        MenuItem menuItem = (menuItemSupplier == null) ? DisplayItem.of(new ItemStack(Material.AIR)) : menuItemSupplier.get();
        this.contents.set(slot, menuItem);
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