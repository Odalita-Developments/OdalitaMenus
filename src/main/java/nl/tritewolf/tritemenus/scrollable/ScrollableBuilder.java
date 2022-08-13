package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public sealed interface ScrollableBuilder permits ScrollableBuilderImpl {

    static @NotNull ScrollableBuilder builder(@NotNull InventoryContents contents, @NotNull String id,
                                              @MagicConstant(intValues = {1, 2, 3, 4, 5, 6}) int showYAxis,
                                              @MagicConstant(intValues = {1, 2, 3, 4, 5, 6, 7, 8, 9}) int showXAxis) {
        return new ScrollableBuilderImpl(contents, id, showYAxis, showXAxis);
    }

    @NotNull ScrollableBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

    @NotNull ScrollableSingleBuilder single(
            @MagicConstant(intValues = {0, 1, 2, 3, 4, 5}) int startRow,
            @MagicConstant(intValues = {0, 1, 2, 3, 4, 5, 6, 7, 8}) int startColumn);

    @NotNull ScrollablePatternBuilder pattern(@MagicConstant(intValues = {0, 1, 2, 3, 4, 5}) int startRow,
                                              @MagicConstant(intValues = {0, 1, 2, 3, 4, 5, 6, 7, 8}) int startColumn,
                                              @NotNull Class<? extends ScrollableDirectionPattern> patternClass);

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