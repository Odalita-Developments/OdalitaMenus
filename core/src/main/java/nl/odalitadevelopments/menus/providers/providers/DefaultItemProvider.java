package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface DefaultItemProvider {

    @NotNull ItemStack closeItem();

    @NotNull ItemStack backItem(@NotNull MenuProvider menuProvider);

    @NotNull ItemStack nextPageItem(@NotNull Pagination pagination);

    @NotNull ItemStack previousPageItem(@NotNull Pagination pagination);

    @NotNull ItemStack scrollUpItem(@NotNull Scrollable scrollable);

    @NotNull ItemStack scrollDownItem(@NotNull Scrollable scrollable);

    @NotNull ItemStack scrollLeftItem(@NotNull Scrollable scrollable);

    @NotNull ItemStack scrollRightItem(@NotNull Scrollable scrollable);
}