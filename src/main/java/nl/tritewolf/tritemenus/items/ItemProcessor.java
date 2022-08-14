package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.function.Supplier;

public final class ItemProcessor {

    public void initializeItems(MenuSession menuSession, InventoryContents inventoryContents) {
        MenuType menuType = menuSession.getMenuType();
        Inventory inventory = menuSession.getInventory();

        MenuItem[][] contents = menuSession.getContents();

        for (Pagination pagination : menuSession.getCache().getPaginationMap().values()) {
            Supplier<MenuItem>[] itemsOnPage = pagination.getItemsOnPage();
            if (itemsOnPage == null) {
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
                inventory.setItem(slot, menuItem.getItemStack());

                if (!menuSession.isHasUpdatableItems() && menuItem.isUpdatable()) {
                    menuSession.setHasUpdatableItems(true);
                }
            }
        }
    }
}