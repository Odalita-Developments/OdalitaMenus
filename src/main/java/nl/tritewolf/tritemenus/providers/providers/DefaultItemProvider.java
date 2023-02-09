package nl.tritewolf.tritemenus.providers.providers;

import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
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