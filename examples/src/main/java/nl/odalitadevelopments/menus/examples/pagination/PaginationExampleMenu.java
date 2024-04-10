package nl.odalitadevelopments.menus.examples.pagination;

import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.examples.common.ItemBuilder;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.buttons.PageItem;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Menu(
        title = "Pagination Example",
        type = MenuType.CHEST_6_ROW
)
public final class PaginationExampleMenu implements PlayerMenuProvider {

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents) {
        Pagination pagination = contents.pagination("example_pagination", 36) // 36 is items per page
                .asyncPageSwitching(false) // Optionally, default is false
                .iterator(contents.createIterator("example_paginaton_iterator", MenuIteratorType.HORIZONTAL, 0, 0))
                .create();

        for (int i = 0; i < 36 * 2; i++) { // Add 36 * 2 (2 pages) of items
            final int finalIndex = i;
            pagination.addItem(() -> DisplayItem.of(ItemBuilder.of(Material.PAPER, "Test Item " + finalIndex).build()));
        }

        contents.set(45, PageItem.previous(pagination)); // Create previous page item with the itemstack provided in DefaultItemProvider
        contents.set(53, PageItem.next(pagination)); // Create next page item with the itemstack provided in DefaultItemProvider
    }
}