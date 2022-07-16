package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

final class ContinuousPatternScrollable extends AbstractScrollable {

    private final ScrollableDirectionPatternCache patternCache;

    private int lastPatternIndex;
    private int lastIndex;
    private int timesContinued;

    ContinuousPatternScrollable(@NotNull ScrollableBuilderImpl builder) {
        super(builder);

        this.patternCache = builder.getPatternCache();

        this.lastPatternIndex = -1;
        this.lastIndex = 0;
        this.timesContinued = 0;
    }

    @Override
    public synchronized @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItemSupplier) {
        Map.Entry<Integer, Integer> newIndexEntry = this.patternCache.index().entrySet().stream()
                .filter((entry) -> entry.getValue() > this.lastPatternIndex)
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElse(null);

        if (newIndexEntry == null) {
            newIndexEntry = this.patternCache.index().entrySet().stream()
                    .filter((entry) -> entry.getValue() > -1)
                    .min(Comparator.comparingInt(Map.Entry::getValue))
                    .orElse(null);

            if (newIndexEntry == null) {
                return this;
            }

            this.lastPatternIndex = -1;
            this.lastIndex += this.patternCache.height() * this.patternCache.width();
            this.timesContinued++;
        }

        int index = newIndexEntry.getKey() + this.lastIndex;
        if (index <= -1) return this;

        this.lastPatternIndex = newIndexEntry.getValue();

        ScrollableSlotPos scrollableSlotPos = this.createSlotPos(index - this.lastIndex);

        int row = scrollableSlotPos.getRow();
        int column = scrollableSlotPos.getColumn();

        if (this.direction == ScrollableDirection.HORIZONTALLY) {
            column += this.timesContinued * this.patternCache.width();
        } else if (this.direction == ScrollableDirection.VERTICALLY) {
            row += this.timesContinued * this.patternCache.height();
        }

        if (row < this.showYAxis && column < this.showXAxis) {
            this.contents.setAsync(SlotPos.of(this.startRow + row, this.startColumn + column), menuItemSupplier.get());
        }

        this.items.put(index, menuItemSupplier);
        return this;
    }

    @Override
    public int lastVertical() { // TODO performance updated needed :)
        int usedPatternAmount = (int) Math.floor((double) this.items.size() / (double) this.patternCache.amountOfIndexes());
        int restItems = this.items.size() - usedPatternAmount * this.patternCache.amountOfIndexes();

        int offsetIndex = -1;
        for (Map.Entry<Integer, Integer> entry : this.patternCache.index().entrySet()) {
            if (entry.getValue() == restItems) {
                offsetIndex = entry.getKey();
                break;
            }
        }

        int offset = (offsetIndex == -1) ? 0 : this.createSlotPos(offsetIndex).getRow() + 1;
        return Math.max(0, usedPatternAmount * this.patternCache.height() - this.showYAxis + offset);
    }

    @Override
    public int lastHorizontal() { // TODO performance updated needed :)
        int usedPatternAmount = (int) Math.floor((double) this.items.size() / (double) this.patternCache.amountOfIndexes());
        int restItems = this.items.size() - usedPatternAmount * this.patternCache.amountOfIndexes();

        int offsetIndex = -1;
        for (Map.Entry<Integer, Integer> entry : this.patternCache.index().entrySet()) {
            if (entry.getValue() == restItems) {
                offsetIndex = entry.getKey();
                break;
            }
        }

        int offset = (offsetIndex == -1) ? 0 : this.createSlotPos(offsetIndex).getColumn() + 1;
        return Math.max(0, usedPatternAmount * this.patternCache.width() - this.showXAxis + offset);
    }

    @Override
    @NotNull Scrollable open(int newAxis, @NotNull ScrollableDirection direction) {
        if (!this.isValidDirection(direction)) return this;

        int lastAxis = (direction == ScrollableDirection.HORIZONTALLY) ? this.lastHorizontal() : this.lastVertical();
        if (newAxis > lastAxis) return this;

        newAxis = Math.max(0, newAxis);

        Map<Integer, Supplier<MenuItem>> pageItems = this.getPageItems(newAxis, direction);

        boolean updatable = this.contents.getTriteMenu().isHasUpdatableItems();
        if (updatable) {
            this.contents.getTriteMenu().setHasUpdatableItems(false);
        }

        this.setNewPage(newAxis, direction);

        for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
            int offset = (direction == ScrollableDirection.HORIZONTALLY) ? newAxis * this.showYAxis : newAxis * this.showXAxis;
            ScrollableSlotPos scrollableSlotPos = this.createSlotPos(entry.getKey() - offset);

            int row = scrollableSlotPos.getRow();
            int column = scrollableSlotPos.getColumn();

            if (row < this.startRow || column < this.startColumn || row > this.showYAxis || column > this.showXAxis) {
                continue;
            }

            if (updatable) {
                MenuItem menuItem = this.contents.getTriteMenu().getContents()[row][column];
                if (menuItem != null && menuItem.isUpdatable()) {
                    this.contents.getTriteMenu().getContents()[row][column] = null;
                }
            }

            this.updateItem(SlotPos.of(row, column).getSlot(), entry.getValue());
        }

        return this;
    }

    private Map<Integer, Supplier<MenuItem>> getPageItems(int newAxis, ScrollableDirection direction) {
        int startIndex = (direction == ScrollableDirection.HORIZONTALLY) ? newAxis * this.showYAxis : newAxis * this.showXAxis;
        int endIndex = (direction == ScrollableDirection.HORIZONTALLY) ? this.showYAxis * this.showXAxis + newAxis * this.showYAxis : this.showYAxis * this.showXAxis + newAxis * this.showXAxis;

        Map<Integer, Supplier<MenuItem>> pageItems = new HashMap<>(this.items.subMap(startIndex, endIndex));

        for (int i = startIndex; i < endIndex; i++) {
            pageItems.putIfAbsent(i, null);
        }

        return pageItems;
    }

    private ScrollableSlotPos createSlotPos(int index) {
        return ScrollableSlotPos.of(
                ScrollableDirection.valueOf(this.patternCache.patternDirection().name()),
                this.patternCache.height(),
                this.patternCache.width(),
                index
        );
    }
}