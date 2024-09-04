package nl.odalitadevelopments.menus.examples.confirm_menu;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface ConfirmMenuBuilder permits ConfirmMenuBuilderImpl {

    @NotNull
    ConfirmMenuBuilder title(@NotNull String title);

    @NotNull
    ConfirmMenuBuilder title(@NotNull Function<@NotNull MenuContents, @NotNull String> title, boolean updatable);

    @NotNull
    ConfirmMenuBuilder title(@NotNull Function<@NotNull MenuContents, @NotNull String> title);

    @NotNull
    ConfirmMenuBuilder response(@NotNull Consumer<@NotNull Boolean> response);

    @NotNull
    ConfirmMenuBuilder centerButtons(MenuItem @Nullable ... centerButtons);

    @NotNull
    ConfirmMenuBuilder centerButtons(@NotNull Function<@NotNull MenuContents, MenuItem[]> centerButtons);

    @NotNull
    ConfirmMenuBuilder closeAfter(boolean closeAfter);

    @NotNull
    ConfirmMenuBuilder buttonSize(@NotNull ConfirmMenu.ButtonSize buttonSize);

    @NotNull
    ConfirmMenuBuilder confirmItem(@NotNull MenuItem confirmItem);

    @NotNull
    ConfirmMenuBuilder confirmItem(@NotNull Function<@NotNull MenuContents, @NotNull MenuItem> confirmItem);

    @NotNull
    ConfirmMenuBuilder cancelItem(@NotNull MenuItem cancelItem);

    @NotNull
    ConfirmMenuBuilder cancelItem(@NotNull Function<@NotNull MenuContents, @NotNull MenuItem> cancelItem);

    @NotNull
    ConfirmMenuBuilder readableTimeDelay(int seconds);

    @NotNull
    ConfirmMenuBuilder globalCacheKey(@NotNull String globalCacheKey);

    void open(@NotNull OdalitaMenus instance, @NotNull Player player);
}