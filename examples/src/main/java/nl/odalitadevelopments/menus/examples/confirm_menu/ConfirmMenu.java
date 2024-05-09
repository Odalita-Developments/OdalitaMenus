package nl.odalitadevelopments.menus.examples.confirm_menu;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.examples.common.ItemBuilder;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Menu(
        title = "",
        type = MenuType.CHEST_3_ROW// Not required, default
)

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ConfirmMenu implements PlayerMenuProvider {

    public static @NotNull ConfirmMenuBuilder builder() {
        return new ConfirmMenuBuilderImpl();
    }

    public static final String DELAY_CACHE_ID = "readable-time-delay";

    private final ConfirmMenuBuilderImpl builder;

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents) {
        if (this.builder.getGlobalCacheKey() != null) {
            contents.setGlobalCacheKey(this.builder.getGlobalCacheKey());
        }

        if (!this.builder.isUpdatableTitle() || this.builder.getReadableTimeDelay() == 0) {
            contents.setTitle(this.builder.getTitle().apply(contents));
        }

        if (this.builder.getReadableTimeDelay() > 0) {
            contents.scheduler().schedule("readable-time-delay", () -> {
                int readableTimeDelay = contents.cache(DELAY_CACHE_ID, this.builder.getReadableTimeDelay() + 1);
                contents.setCache(DELAY_CACHE_ID, readableTimeDelay - 1);

                if (this.builder.isUpdatableTitle()) {
                    contents.setTitle(this.builder.getTitle().apply(contents));
                }
            }, 20, this.builder.getReadableTimeDelay());
        }

        ConfirmItem confirmItem = new ConfirmItem(contents, this.builder, true,
                (this.builder.getConfirmItem() == null)
                        ? DisplayItem.of(ItemBuilder.of(Material.LIME_WOOL, "&aConfirm").build())
                        : this.builder.getConfirmItem().apply(contents)
        );

        ConfirmItem cancelItem = new ConfirmItem(contents, this.builder, false,
                (this.builder.getCancelItem() == null)
                        ? DisplayItem.of(ItemBuilder.of(Material.RED_WOOL, "&cCancel").build())
                        : this.builder.getCancelItem().apply(contents)
        );

        if (this.builder.getButtonSize() == ButtonSize.SMALL) {
            contents.set(11, confirmItem);
            contents.set(15, cancelItem);
        } else {
            for (int i = 0; i < 3; i++) {
                contents.fillColumn(i, confirmItem);
            }

            for (int i = 6; i < 9; i++) {
                contents.fillColumn(i, cancelItem);
            }
        }

        MenuItem[] centerButtons = this.builder.getCenterButtons().apply(contents);
        if (centerButtons != null) {
            for (int i = 0; i < centerButtons.length; ++i) {
                if (i > 3) break;

                if (centerButtons[i] != null) {
                    contents.set(i, 4, centerButtons[i]);
                }
            }
        }
    }

    public enum ButtonSize {

        SMALL, // 1 item
        BIG // 3x3 items
    }

    private static final class ConfirmItem extends MenuItem {

        private final MenuContents contents;
        private final ConfirmMenuBuilderImpl builder;
        private final boolean confirm;
        private final MenuItem menuItem;

        private ConfirmItem(MenuContents contents, ConfirmMenuBuilderImpl builder, boolean confirm, MenuItem menuItem) {
            this.contents = contents;
            this.builder = builder;
            this.confirm = confirm;
            this.menuItem = menuItem;
        }

        @Override
        public @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
            if (this.menuItem.isUpdatable()) {
                if (this.confirm) {
                    return this.builder.getConfirmItem().apply(this.contents).provideItem(instance, contents);
                } else {
                    return this.builder.getCancelItem().apply(this.contents).provideItem(instance, contents);
                }
            }

            return this.menuItem.provideItem(instance, contents);
        }

        @Override
        public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
            return (event) -> {
                if (this.contents.scheduler().isRunning("readable-time-delay")) {
                    return;
                }

                if (this.builder.getResponse() != null) {
                    this.builder.getResponse().accept(this.confirm);
                }

                if (this.builder.isCloseAfter()) {
                    event.getWhoClicked().closeInventory();
                }
            };
        }

        @Override
        public boolean isUpdatable() {
            return this.menuItem.isUpdatable();
        }

        @Override
        public int getUpdateTicks() {
            return this.menuItem.getUpdateTicks();
        }
    }
}