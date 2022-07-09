package nl.tritewolf.tritemenus.scrollable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.scrollable.pattern.DirectionScrollablePattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
public final class ScrollableBuilder {

    private final InventoryContents contents;
    private final String id;
    private final int showYAxis;
    private final int showXAxis;

    private List<Supplier<MenuItem>> items = new ArrayList<>();

    private int startRow;
    private int startColumn;

    private boolean isSingle;
    private PatternDirection direction;
    private DirectionScrollablePattern pattern;

    @ApiStatus.Internal
    @NotNull ScrollableBuilder setDirection(@NotNull PatternDirection direction) {
        this.direction = direction;
        return this;
    }

    public @NotNull ScrollableBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items) {
        this.items = items;
        return this;
    }

    public @NotNull SingleBuilder single(int startRow, int startColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isSingle = true;
        return new SingleBuilder(this);
    }

    public @NotNull PatternBuilder pattern(int startRow, int startColumn, @NotNull DirectionScrollablePattern pattern) { // TODO add pattern
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isSingle = false;
        this.pattern = pattern;
        return new PatternBuilder(this);
    }

    public @NotNull Scrollable create() {
        return new Scrollable(this);
    }

    public enum SingleDirection {

        VERTICALLY,
        HORIZONTALLY
    }

    public enum PatternDirection {

        VERTICALLY,
        HORIZONTALLY,
        ALL
    }

    @RequiredArgsConstructor
    public static final class SingleBuilder {

        private final ScrollableBuilder builder;

        public ScrollableBuilder direction(@NotNull SingleDirection direction) {
            return this.builder.setDirection((direction == SingleDirection.HORIZONTALLY) ? PatternDirection.HORIZONTALLY : PatternDirection.VERTICALLY);
        }
    }

    @RequiredArgsConstructor
    public static final class PatternBuilder {

        private final ScrollableBuilder builder;

        public ScrollableBuilder direction(@NotNull PatternDirection direction) {
            return this.builder.setDirection(direction);
        }
    }
}