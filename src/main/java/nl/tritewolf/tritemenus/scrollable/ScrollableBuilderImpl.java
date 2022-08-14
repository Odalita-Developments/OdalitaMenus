package nl.tritewolf.tritemenus.scrollable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.patterns.PatternContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
final class ScrollableBuilderImpl implements ScrollableBuilder {

    private final InventoryContents contents;
    private final String id;
    private final int showYAxis;
    private final int showXAxis;

    private List<Supplier<MenuItem>> items = new ArrayList<>();

    private int startRow;
    private int startColumn;

    private boolean isSingle;
    private ScrollableDirection direction;
    private ScrollableDirectionPatternCache patternCache;
    private boolean repeatedPattern;

    @Override
    public @NotNull ScrollableBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items) {
        this.items = items;
        return this;
    }

    @Override
    public @NotNull ScrollableSingleBuilder single(int startRow, int startColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isSingle = true;
        return new ScrollableSingleBuilderImpl(this);
    }

    @Override
    public @NotNull ScrollablePatternBuilder pattern(int startRow, int startColumn, @NotNull Class<? extends ScrollableDirectionPattern> patternClass) {
        PatternContainer patternContainer = TriteMenus.getTriteMenus().getTriteJection(PatternContainer.class);
        ScrollableDirectionPatternCache patternCache = patternContainer.getPattern(patternClass);
        if (patternCache == null) {
            throw new IllegalArgumentException("No scrollable pattern found for class: '" + patternClass.getName() + "'");
        }

        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isSingle = false;
        this.patternCache = patternCache;
        return new ScrollablePatternBuilderImpl(this);
    }

    @Override
    public @NotNull Scrollable create() {
        AbstractScrollable scrollable;
        if (this.isSingle) {
            scrollable = new SingleScrollable(this);
        } else if (this.repeatedPattern) {
            if ((this.direction == ScrollableDirection.HORIZONTALLY && this.patternCache.patternDirection() != ScrollableDirectionPattern.Direction.VERTICALLY)
                    || (this.direction == ScrollableDirection.VERTICALLY && this.patternCache.patternDirection() != ScrollableDirectionPattern.Direction.HORIZONTALLY)) {
                throw new IllegalArgumentException("Pattern direction does not match with scrollable direction.");
            }

            scrollable = new RepeatedPatternScrollable(this);
        } else {
            scrollable = new PatternScrollable(this);
        }

        this.contents.menuSession().getCache().getScrollableMap().put(this.id, scrollable);

        scrollable.initItems();

        return scrollable;
    }

    @RequiredArgsConstructor
    public static final class ScrollableSingleBuilderImpl implements ScrollableSingleBuilder {

        private final ScrollableBuilderImpl builder;

        @Override
        public @NotNull Scrollable horizontally() {
            this.builder.direction = ScrollableDirection.HORIZONTALLY;
            return this.builder.create();
        }

        @Override
        public @NotNull Scrollable vertically() {
            this.builder.direction = ScrollableDirection.VERTICALLY;
            return this.builder.create();
        }
    }

    @RequiredArgsConstructor
    public static final class ScrollablePatternBuilderImpl implements ScrollablePatternBuilder {

        private final ScrollableBuilderImpl builder;

        @Override
        public @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder horizontally() {
            this.builder.direction = ScrollableDirection.HORIZONTALLY;
            return new ScrollableRepeatedPatternBuilderImpl(this.builder);
        }

        @Override
        public @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder vertically() {
            this.builder.direction = ScrollableDirection.VERTICALLY;
            return new ScrollableRepeatedPatternBuilderImpl(this.builder);
        }

        @Override
        public @NotNull Scrollable horizontallyAndVertically() {
            this.builder.direction = ScrollableDirection.ALL;
            return this.builder.create();
        }
    }

    @RequiredArgsConstructor
    public static final class ScrollableRepeatedPatternBuilderImpl implements ScrollableRepeatedPatternBuilder {

        private final ScrollableBuilderImpl builder;

        @Override
        public @NotNull Scrollable repeated() {
            this.builder.repeatedPattern = true;
            return this.builder.create();
        }

        @Override
        public @NotNull Scrollable once() {
            this.builder.repeatedPattern = false;
            return this.builder.create();
        }

        @Override
        public @NotNull Scrollable repeated(boolean repeated) {
            this.builder.repeatedPattern = repeated;
            return this.builder.create();
        }

        @Override
        public @NotNull Scrollable once(boolean once) {
            return this.repeated(!once);
        }
    }
}