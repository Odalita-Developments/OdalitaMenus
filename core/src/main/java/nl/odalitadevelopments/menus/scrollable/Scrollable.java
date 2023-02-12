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
    void setInitialized(boolean initialized);

    @ApiStatus.Internal
    @NotNull Map<Integer, Supplier<MenuItem>> getPageItems();
}