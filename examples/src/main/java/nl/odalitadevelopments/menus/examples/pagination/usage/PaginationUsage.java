package nl.odalitadevelopments.menus.examples.pagination.usage;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.examples.pagination.ObjectPaginationExampleMenu;
import nl.odalitadevelopments.menus.examples.pagination.PaginationExampleMenu;
import org.bukkit.entity.Player;

final class PaginationUsage {

    public void openPagination(OdalitaMenus instance, Player player) {
        instance.openMenu(new PaginationExampleMenu(), player);
    }

    public void openPaginationWithPredefinedPage(OdalitaMenus instance, Player player, int page) {
        instance.openMenuBuilder(new PaginationExampleMenu(), player)
                .pagination("example_pagination", page) // Use same id as provided in the menu
                .open();
    }

    public void openObjectPagination(OdalitaMenus instance, Player player) {
        instance.openMenu(new ObjectPaginationExampleMenu(), player);
    }

    public void openObjectPaginationWithPredefinedPage(OdalitaMenus instance, Player player, int page) {
        instance.openMenuBuilder(new ObjectPaginationExampleMenu(), player)
                .pagination("player_example_pagination", page) // Use same id as provided in the menu
                .open();
    }
}