package nl.odalitadevelopments.menus.scrollable;

import nl.odalitadevelopments.menus.utils.Pair;
import org.jetbrains.annotations.NotNull;

final class SingleScrollable extends AbstractScrollable {

    SingleScrollable(@NotNull ScrollableBuilderImpl builder) {
        super(builder);
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
    protected int newItemIndex() {
        return (this.items.isEmpty()) ? 0 : this.items.lastKey() + 1;
    }

    @Override
    protected @NotNull Pair<Integer, Integer> getStartEndIndexes() {
        int startIndex = (this.direction == ScrollableDirection.HORIZONTALLY)
                ? this.currentXAxis * this.showYAxis
                : this.currentYAxis * this.showXAxis;

        int endIndex = (this.direction == ScrollableDirection.HORIZONTALLY)
                ? (this.currentXAxis + this.showXAxis) * this.showYAxis
                : (this.currentYAxis + this.showYAxis) * this.showXAxis;

        return new Pair<>(startIndex, endIndex);
    }

    @Override
    protected int getIndexOffset(int index, int axis, @NotNull ScrollableDirection direction) {
        return (direction == ScrollableDirection.HORIZONTALLY)
                ? axis * this.showYAxis
                : axis * this.showXAxis;
    }

    @Override
    protected @NotNull ScrollableSlotPos createSlotPos(int index) {
        return ScrollableSlotPos.SingleScrollableSlotPos.of(this.direction, this.showYAxis, this.showXAxis, index);
    }
}