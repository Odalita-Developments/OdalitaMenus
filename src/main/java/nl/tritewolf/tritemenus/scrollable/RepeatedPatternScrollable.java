package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

final class RepeatedPatternScrollable extends PatternScrollable {

    private final ScrollableDirectionPatternCache patternCache;

    private int lastPatternIndex = -1;
    private int lastIndex = 0;

    private int lastVertical = -1;
    private int lastHorizontal = -1;

    RepeatedPatternScrollable(@NotNull ScrollableBuilderImpl builder) {
        super(builder);

        this.patternCache = builder.getPatternCache();
    }

    @Override
    public int lastVertical() {
        if (this.lastVertical == -1) {
            this.calculateLastVertical();
        }

        return this.lastVertical;
    }

    @Override
    public int lastHorizontal() {
        if (this.lastHorizontal == -1) {
            this.calculateLastHorizontal();
        }

        return this.lastHorizontal;
    }

    @Override
    protected int newItemIndex() {
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
                return -1;
            }

            this.lastPatternIndex = -1;
            this.lastIndex += this.patternCache.height() * this.patternCache.width();
        }

        int index = newIndexEntry.getKey() + this.lastIndex;
        if (index <= -1) return -1;

        this.lastPatternIndex = newIndexEntry.getValue();

        if (this.direction == ScrollableDirection.HORIZONTALLY && this.lastHorizontal != -1) {
            this.lastHorizontal = -1;
        } else if (this.direction == ScrollableDirection.VERTICALLY && this.lastVertical != -1) {
            this.lastVertical = -1;
        }

        return index;
    }

    @Override
    protected @NotNull Pair<Integer, Integer> getStartEndIndexes() {
        int startIndex = (this.direction == ScrollableDirection.HORIZONTALLY)
                ? this.currentXAxis * this.showYAxis
                : this.currentYAxis * this.showXAxis;

        int endIndex = (this.direction == ScrollableDirection.HORIZONTALLY)
                ? this.showYAxis * this.showXAxis + this.currentXAxis * this.showYAxis
                : this.showYAxis * this.showXAxis + this.currentYAxis * this.showXAxis;

        return new Pair<>(startIndex, endIndex);
    }

    @Override
    protected int getIndexOffset(int index, int axis, @NotNull ScrollableDirection direction) {
        return (direction == ScrollableDirection.HORIZONTALLY) ? axis * this.patternCache.height() : axis * this.patternCache.width();
    }

    @Override
    protected @NotNull ScrollableSlotPos createSlotPos(int index) {
        return ScrollableSlotPos.of(
                ScrollableDirection.valueOf(this.patternCache.patternDirection().name()),
                this.showYAxis,
                this.showXAxis,
                index
        );
    }

    @Override
    protected @NotNull Pair<@NotNull Integer, @NotNull Integer> rowColumnModifier(int row, int column) {
        return new Pair<>(row, column);
    }

    private void calculateLastVertical() {
        LastPageData lastPageData = this.calculateLastPageData();
        int usedPatternAmount = lastPageData.usedPatternAmount();
        int offsetIndex = lastPageData.offsetIndex();

        int offset = (offsetIndex == -1) ? 0 : this.createSlotPos(offsetIndex).getRow() + 1;
        this.lastVertical = Math.max(0, usedPatternAmount * this.patternCache.height() - this.showYAxis + offset);
    }

    private void calculateLastHorizontal() {
        LastPageData lastPageData = this.calculateLastPageData();
        int usedPatternAmount = lastPageData.usedPatternAmount();
        int offsetIndex = lastPageData.offsetIndex();

        int offset = (offsetIndex == -1) ? 0 : this.createSlotPos(offsetIndex).getColumn() + 1;
        this.lastHorizontal = Math.max(0, usedPatternAmount * this.patternCache.width() - this.showXAxis + offset);
    }

    private LastPageData calculateLastPageData() {
        int usedPatternAmount = (int) Math.floor((double) this.items.size() / (double) this.patternCache.amountOfIndexes());
        int restItems = this.items.size() - usedPatternAmount * this.patternCache.amountOfIndexes();

        int offsetIndex = -1;
        for (Map.Entry<Integer, Integer> entry : this.patternCache.index().entrySet()) {
            if (entry.getValue() == restItems) {
                offsetIndex = entry.getKey();
                break;
            }
        }

        return new LastPageData(usedPatternAmount, offsetIndex);
    }

    record LastPageData(int usedPatternAmount, int offsetIndex) {
    }
}