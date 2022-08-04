package nl.tritewolf.tritemenus.scrollable;

import nl.tritewolf.tritemenus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

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
}