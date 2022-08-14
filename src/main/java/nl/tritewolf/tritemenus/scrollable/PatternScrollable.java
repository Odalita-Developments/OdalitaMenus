package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

sealed class PatternScrollable extends AbstractScrollable permits RepeatedPatternScrollable {

    private final ScrollableDirectionPatternCache.Cache patternCache;

    private int lastPatternIndex;

    PatternScrollable(@NotNull ScrollableBuilderImpl builder) {
        super(builder);

        this.patternCache = builder.getPatternCache();

        this.lastPatternIndex = -1;
    }

    @Override
    public int lastVertical() {
        return this.patternCache.height() - this.showYAxis;
    }

    @Override
    public int lastHorizontal() {
        return this.patternCache.width() - this.showXAxis;
    }

    @Override
    protected int newItemIndex() {
        Map.Entry<Integer, Integer> newIndexEntry = this.patternCache.index().entrySet().stream()
                .filter((entry) -> entry.getValue() > this.lastPatternIndex)
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElse(null);

        int index;
        if (newIndexEntry == null || (index = newIndexEntry.getKey()) <= -1) {
            return -1;
        }

        this.lastPatternIndex = newIndexEntry.getValue();
        return index;
    }

    @Override
    protected @NotNull Pair<Integer, Integer> getStartEndIndexes() {
        int startIndex = this.createSlotPos(
                this.currentYAxis,
                this.currentXAxis
        ).getSlot();

        int endIndex = this.createSlotPos(
                this.showYAxis + this.currentYAxis - 1,
                this.showXAxis + this.currentXAxis
        ).getSlot();

        return new Pair<>(startIndex, endIndex);
    }

    @Override
    protected int getIndexOffset(int index, int axis, @NotNull ScrollableDirection direction) {
        return 0;
    }

    @Override
    protected @NotNull ScrollableSlotPos createSlotPos(int index) {
        return ScrollableSlotPos.of(this.patternCache.height(), this.patternCache.width(), index);
    }

    @Override
    protected @NotNull Pair<@NotNull Integer, @NotNull Integer> rowColumnModifier(int row, int column) {
        return new Pair<>(
                row - this.currentYAxis,
                column - this.currentXAxis
        );
    }

    private @NotNull ScrollableSlotPos createSlotPos(int row, int column) {
        return ScrollableSlotPos.of(this.patternCache.height(), this.patternCache.width(), row, column);
    }
}