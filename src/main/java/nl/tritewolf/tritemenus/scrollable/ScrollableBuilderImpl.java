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
    private boolean continuousPattern;

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
        if (this.isSingle) {
            return new SingleScrollable(this);
        }

        if (this.continuousPattern) {
            if ((this.direction == ScrollableDirection.HORIZONTALLY && this.patternCache.patternDirection() != ScrollableDirectionPattern.Direction.VERTICALLY)
                    || (this.direction == ScrollableDirection.VERTICALLY && this.patternCache.patternDirection() != ScrollableDirectionPattern.Direction.HORIZONTALLY)) {
                throw new IllegalArgumentException("Pattern direction does not match with scrollable direction.");
            }

            return new ContinuousPatternScrollable(this);
        } else {
            return new PatternScrollable(this);
        }
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
        public @NotNull ScrollableContinuousPatternBuilder horizontally() {
            this.builder.direction = ScrollableDirection.HORIZONTALLY;
            return new ScrollableContinuousPatternBuilderImpl(this.builder);
        }

        @Override
        public @NotNull ScrollableContinuousPatternBuilder vertically() {
            this.builder.direction = ScrollableDirection.VERTICALLY;
            return new ScrollableContinuousPatternBuilderImpl(this.builder);
        }

        @Override
        public @NotNull Scrollable horizontallyAndVertically() {
            this.builder.direction = ScrollableDirection.ALL;
            return this.builder.create();
        }
    }

    @RequiredArgsConstructor
    public static final class ScrollableContinuousPatternBuilderImpl implements ScrollableContinuousPatternBuilder {

        private final ScrollableBuilderImpl builder;

        @Override
        public @NotNull Scrollable continuous() {
            this.builder.continuousPattern = true;
            return this.builder.create();
        }

        @Override
        public @NotNull Scrollable continuous(boolean continuous) {
            this.builder.continuousPattern = continuous;
            return this.builder.create();
        }
    }
}