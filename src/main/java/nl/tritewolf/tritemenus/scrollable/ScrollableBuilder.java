package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public sealed interface ScrollableBuilder permits ScrollableBuilderImpl {

    static @NotNull ScrollableBuilder builder(@NotNull InventoryContents contents, @NotNull String id, int showYAxis, int showXAxis) {
        return new ScrollableBuilderImpl(contents, id, showYAxis, showXAxis);
    }

    @NotNull ScrollableBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

    @NotNull ScrollableSingleBuilder single(int startRow, int startColumn);

    @NotNull ScrollablePatternBuilder pattern(int startRow, int startColumn, @NotNull Class<? extends ScrollableDirectionPattern> patternClass);

    @NotNull Scrollable create();

    sealed interface ScrollableSingleBuilder permits ScrollableBuilderImpl.ScrollableSingleBuilderImpl {

        @NotNull Scrollable horizontally();

        @NotNull Scrollable vertically();
    }

    sealed interface ScrollablePatternBuilder permits ScrollableBuilderImpl.ScrollablePatternBuilderImpl {

        @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder horizontally();

        @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder vertically();

        @NotNull Scrollable horizontallyAndVertically();
    }

    sealed interface ScrollableRepeatedPatternBuilder permits ScrollableBuilderImpl.ScrollableRepeatedPatternBuilderImpl {

        @NotNull Scrollable repeated();

        @NotNull Scrollable once();

        @NotNull Scrollable repeated(boolean repeated);

        @NotNull Scrollable once(boolean once);
    }
}