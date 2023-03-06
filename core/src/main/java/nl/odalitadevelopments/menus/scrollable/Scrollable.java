package nl.odalitadevelopments.menus.scrollable;

import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public sealed interface Scrollable permits AbstractScrollable {

    @NotNull Scrollable addItem(@NotNull Supplier<@NotNull MenuItem> menuItemSupplier);

    int currentVertical();

    int currentHorizontal();

    int lastVertical();

    int lastHorizontal();

    boolean isFirstVertical();

    boolean isFirstHorizontal();

    boolean isLastVertical();

    boolean isLastHorizontal();

    @NotNull Scrollable openVertical(int newYAxis);

    @NotNull Scrollable openHorizontal(int newXAxis);

    @NotNull Scrollable nextVertical();

    @NotNull Scrollable previousVertical();

    @NotNull Scrollable nextHorizontal();

    @NotNull Scrollable previousHorizontal();

    @NotNull Scrollable next();

    @NotNull Scrollable previous();

    @ApiStatus.Internal
    void setAxes(int xAxis, int yAxis);

    @ApiStatus.Internal
    void setInitialized();

    @ApiStatus.Internal
    @NotNull Map<Integer, Supplier<MenuItem>> getPageItems();
}