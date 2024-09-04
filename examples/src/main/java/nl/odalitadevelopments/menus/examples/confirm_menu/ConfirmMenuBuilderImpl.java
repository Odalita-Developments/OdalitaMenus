package nl.odalitadevelopments.menus.examples.confirm_menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter(AccessLevel.PACKAGE)
final class ConfirmMenuBuilderImpl implements ConfirmMenuBuilder {

    private Function<MenuContents, String> title = (contents) -> "";
    private boolean updatableTitle = false;

    private Consumer<Boolean> response;
    private Function<MenuContents, MenuItem[]> centerButtons = (contents) -> new MenuItem[0];
    private boolean closeAfter = true;

    private ConfirmMenu.ButtonSize buttonSize = ConfirmMenu.ButtonSize.BIG;
    private Function<MenuContents, MenuItem> confirmItem;
    private Function<MenuContents, MenuItem> cancelItem;

    private int readableTimeDelay = 0;

    private String globalCacheKey = "";

    @Override
    public @NotNull ConfirmMenuBuilder title(@NotNull String title) {
        return this.title((contents) -> title);
    }

    @Override
    public @NotNull ConfirmMenuBuilder title(@NotNull Function<@NotNull MenuContents, @NotNull String> title, boolean updatable) {
        this.title = title;
        this.updatableTitle = updatable;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder title(@NotNull Function<@NotNull MenuContents, @NotNull String> title) {
        return this.title(title, false);
    }

    @Override
    public @NotNull ConfirmMenuBuilder response(@NotNull Consumer<@NotNull Boolean> response) {
        this.response = response;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder centerButtons(MenuItem @Nullable ... centerButtons) {
        return this.centerButtons((contents) -> centerButtons);
    }

    @Override
    public @NotNull ConfirmMenuBuilder centerButtons(@NotNull Function<@NotNull MenuContents, MenuItem[]> centerButtons) {
        this.centerButtons = centerButtons;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder closeAfter(boolean closeAfter) {
        this.closeAfter = closeAfter;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder buttonSize(@NotNull ConfirmMenu.ButtonSize buttonSize) {
        this.buttonSize = buttonSize;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder confirmItem(@NotNull MenuItem confirmItem) {
        return this.confirmItem((contents) -> confirmItem);
    }

    @Override
    public @NotNull ConfirmMenuBuilder confirmItem(@NotNull Function<@NotNull MenuContents, @NotNull MenuItem> confirmItem) {
        this.confirmItem = confirmItem;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder cancelItem(@NotNull MenuItem cancelItem) {
        return this.cancelItem((contents) -> cancelItem);
    }

    @Override
    public @NotNull ConfirmMenuBuilder cancelItem(@NotNull Function<@NotNull MenuContents, @NotNull MenuItem> cancelItem) {
        this.cancelItem = cancelItem;
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder readableTimeDelay(int seconds) {
        this.readableTimeDelay = Math.max(seconds, 0);
        return this;
    }

    @Override
    public @NotNull ConfirmMenuBuilder globalCacheKey(@NotNull String globalCacheKey) {
        this.globalCacheKey = globalCacheKey;
        return this;
    }

    @Override
    public void open(@NotNull OdalitaMenus instance, @NotNull Player player) {
        instance.openMenu(new ConfirmMenu(this), player);
    }
}