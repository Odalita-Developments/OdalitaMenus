package nl.tritewolf.tritemenus.providers.providers;

import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface DefaultItemProvider {

    default @NotNull ItemStack closeItem() {
        return InventoryUtils.createItemStack(Material.BARRIER, "&cClose");
    }

    default @NotNull ItemStack backItem(@NotNull MenuProvider menuProvider) {
        return InventoryUtils.createItemStack(Material.ARROW, "&cGo back");
    }

    default @NotNull ItemStack nextItem(@NotNull Pagination pagination) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bNext page");
    }

    default @NotNull ItemStack previousItem(@NotNull Pagination pagination) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bPrevious page");
    }

    default @NotNull ItemStack scrollUpItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll up");
    }

    default @NotNull ItemStack scrollDownItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll down");
    }

    default @NotNull ItemStack scrollLeftItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll left");
    }

    default @NotNull ItemStack scrollRightItem(@NotNull Scrollable scrollable) {
        return InventoryUtils.createItemStack(Material.ARROW, "&bScroll right");
    }
}