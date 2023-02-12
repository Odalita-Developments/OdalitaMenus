package nl.odalitadevelopments.menus.examples.providers;

import nl.odalitadevelopments.menus.examples.common.ItemBuilder;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CustomDefaultItemProvider implements DefaultItemProvider {

    @Override
    public @NotNull ItemStack closeItem() {
        return ItemBuilder.of(Material.BARRIER, "&cClose").build();
    }

    @Override
    public @NotNull ItemStack backItem(@NotNull MenuProvider menuProvider) {
        return ItemBuilder.of(Material.BARRIER, "&cBack")
                .lore("&7Go back to " + menuProvider.getMenu().title())
                .build();
    }

    @Override
    public @NotNull ItemStack nextPageItem(@NotNull Pagination pagination) {
        return ItemBuilder.of(Material.BARRIER, "&eNext &b(" + (pagination.getCurrentPage() + 1) + " / " + pagination.lastPage() + ")").build();
    }

    @Override
    public @NotNull ItemStack previousPageItem(@NotNull Pagination pagination) {
        return ItemBuilder.of(Material.BARRIER, "&ePrevious &b(" + (pagination.getCurrentPage() - 1) + " / " + pagination.lastPage() + ")").build();
    }

    @Override
    public @NotNull ItemStack scrollUpItem(@NotNull Scrollable scrollable) {
        return ItemBuilder.of(Material.BARRIER, "&eScroll up &b(" + (scrollable.currentVertical() - 1) + " / " + scrollable.lastVertical() + ")").build();
    }

    @Override
    public @NotNull ItemStack scrollDownItem(@NotNull Scrollable scrollable) {
        return ItemBuilder.of(Material.BARRIER, "&eScroll down &b(" + (scrollable.currentVertical() + 1) + " / " + scrollable.lastVertical() + ")").build();
    }

    @Override
    public @NotNull ItemStack scrollLeftItem(@NotNull Scrollable scrollable) {
        return ItemBuilder.of(Material.BARRIER, "&eScroll left &b(" + (scrollable.currentHorizontal() - 1) + " / " + scrollable.lastHorizontal() + ")").build();
    }

    @Override
    public @NotNull ItemStack scrollRightItem(@NotNull Scrollable scrollable) {
        return ItemBuilder.of(Material.BARRIER, "&eScroll right &b(" + (scrollable.currentHorizontal() + 1) + " / " + scrollable.lastHorizontal() + ")").build();
    }
}