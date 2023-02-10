package nl.odalitadevelopments.menus.scrollable;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public interface ScrollableBuilder {

    static @NotNull ScrollableBuilder builder(@NotNull InventoryContents contents, @NotNull String id, int showYAxis, int showXAxis) {
        return new ScrollableBuilderImpl(contents, id, showYAxis, showXAxis);
    }

    @NotNull ScrollableBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

    @NotNull ScrollableSingleBuilder single(int startRow, int startColumn);

    @NotNull ScrollablePatternBuilder pattern(int startRow, int startColumn, @NotNull Class<? extends ScrollableDirectionPattern> patternClass);

    @NotNull Scrollable create();

    interface ScrollableSingleBuilder {

        @NotNull Scrollable horizontally();

        @NotNull Scrollable vertically();
    }

    interface ScrollablePatternBuilder {

        @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder horizontally();

        @NotNull ScrollableBuilder.ScrollableRepeatedPatternBuilder vertically();

        @NotNull Scrollable horizontallyAndVertically();
    }

    interface ScrollableRepeatedPatternBuilder {

        @NotNull Scrollable repeated();

        @NotNull Scrollable once();

        @NotNull Scrollable repeated(boolean repeated);

        @NotNull Scrollable once(boolean once);
    }
}