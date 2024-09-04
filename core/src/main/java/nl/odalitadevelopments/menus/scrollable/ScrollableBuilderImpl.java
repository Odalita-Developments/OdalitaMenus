package nl.odalitadevelopments.menus.scrollable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.patterns.PatternContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
final class ScrollableBuilderImpl implements ScrollableBuilder {

    private final AbstractMenuSession<?, ?, ?> menuSession;
    private final String id;
    private final int showYAxis;
    private final int showXAxis;

    private List<Supplier<MenuItem>> items = new ArrayList<>();

    private int startRow;
    private int startColumn;

    private boolean isSingle;
    private ScrollableDirection direction;
    private ScrollableDirectionPatternCache.Cache patternCache;
    private boolean repeatedPattern;

    @Getter(AccessLevel.NONE)
    private ScrollableDirectionPatternCache tempPatternCache;

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
        PatternContainer patternContainer = this.menuSession.instance().getPatternContainer();
        ScrollableDirectionPatternCache patternCache = patternContainer.getPattern(patternClass);
        if (patternCache == null) {
            throw new IllegalArgumentException("No scrollable pattern found for class: '" + patternClass.getName() + "'");
        }

        this.startRow = startRow;
        this.startColumn = startColumn;
        this.isSingle = false;
        this.tempPatternCache = patternCache;
        return new ScrollablePatternBuilderImpl(this);
    }

    @Override
    public @NotNull Scrollable create() {
        AbstractScrollable scrollable;
        if (this.isSingle) {
            scrollable = new SingleScrollableImpl(this);
        } else if (this.repeatedPattern) {
            this.patternCache = this.tempPatternCache.initialize(this.direction.getCacheInitializationDirection());

            scrollable = new RepeatedPatternScrollableImpl(this);
        } else {
            this.patternCache = this.tempPatternCache.initialize(this.direction.getCacheInitializationDirection());

            scrollable = new PatternScrollableImpl(this);
        }

        this.menuSession.cache().getScrollableMap().put(this.id, scrollable);

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
        public @NotNull ScrollableRepeatedPatternBuilder horizontally() {
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