package nl.odalitadevelopments.menus.providers.processors;

import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class DefaultItemProcessor implements DefaultItemProvider {

    @Override
    public @NotNull ItemStack closeItem() {
        return InventoryUtils.createItemStack(Material.BARRIER, "&cClose");
    }

    @Override
    public @NotNull ItemStack backItem(@NotNull MenuProvider menuProvider) {
        return InventoryUtils.createItemStack(Material.ARROW, "&cGo back");
    }

    @Override
    public @NotNull ItemStack nextPageItem(@NotNull Pagination pagination) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bNext page");
    }

    @Override
    public @NotNull ItemStack previousPageItem(@NotNull Pagination pagination) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bPrevious page");
    }

    @Override
    public @NotNull ItemStack scrollUpItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll up");
    }

    @Override
    public @NotNull ItemStack scrollDownItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll down");
    }

    @Override
    public @NotNull ItemStack scrollLeftItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll left");
    }

    @Override
    public @NotNull ItemStack scrollRightItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll right");
    }
}