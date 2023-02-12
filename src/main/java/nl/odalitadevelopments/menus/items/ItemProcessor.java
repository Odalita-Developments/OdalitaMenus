package nl.odalitadevelopments.menus.items;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ItemProcessor {

    @ApiStatus.Internal
    public void initializeItems(MenuSession menuSession, InventoryContents inventoryContents) {
        SupportedMenuType menuType = menuSession.getMenuType();
        Inventory inventory = menuSession.getInventory();

        MenuItem[][] contents = menuSession.getContents();

        for (Pagination pagination : menuSession.getCache().getPaginationMap().values()) {
            List<Supplier<MenuItem>> itemsOnPage = pagination.getItemsOnPage();
            if (itemsOnPage.isEmpty()) {
                pagination.setInitialized(true);
                continue;
            }

            for (Supplier<MenuItem> menuItemSupplier : itemsOnPage) {
                if (menuItemSupplier == null) continue;

                MenuItem menuItem = menuItemSupplier.get();
                if (menuItem == null) continue;

                int slot = pagination.getIterator().getSlot();
                if (inventory.getItem(slot) != null) continue;

                inventoryContents.setAsync(slot, menuItem);
                pagination.getIterator().next();
            }

            pagination.setInitialized(true);
        }

        for (Scrollable scrollable : menuSession.getCache().getScrollableMap().values()) {
            Map<Integer, Supplier<MenuItem>> pageItems = scrollable.getPageItems();
            if (pageItems.isEmpty()) {
                scrollable.setInitialized(true);
                continue;
            }

            for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
                int slot = entry.getKey();
                Supplier<MenuItem> menuItemSupplier = entry.getValue();
                if (menuItemSupplier == null) continue;

                MenuItem menuItem = menuItemSupplier.get();
                if (menuItem == null) continue;

                if (inventory.getItem(slot) != null) continue;

                inventoryContents.setAsync(slot, menuItem);
            }

            scrollable.setInitialized(true);
        }

        for (int row = 0; row < menuSession.getRows(); row++) {
            for (int column = 0; column < menuSession.getColumns(); column++) {
                MenuItem menuItem = contents[row][column];
                if (menuItem == null) continue;

                int slot = SlotPos.of(menuType.maxRows(), menuType.maxColumns(), row, column).getSlot();
                inventory.setItem(slot, menuItem.getItemStack(menuSession.getInstance()));

                if (!menuSession.isHasUpdatableItems() && menuItem.isUpdatable()) {
                    menuSession.setHasUpdatableItems(true);
                }
            }
        }
    }
}