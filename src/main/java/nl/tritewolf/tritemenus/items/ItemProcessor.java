package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.pagination.Pagination;
import org.bukkit.inventory.Inventory;

import java.util.function.Supplier;

public final class ItemProcessor {

    public void initializeItems(MenuObject menuObject) {
        MenuItem[][] contents = menuObject.getContents();
        Inventory inventory = menuObject.getInventory();

        for (Pagination pagination : menuObject.getPaginationMap().values()) {

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

                inventory.setItem(slot, menuItem.getItemStack());
                pagination.getIterator().next();
            }
            pagination.setInitialized(true);
        }

        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                MenuItem menuItem = contents[row][column];
                if (menuItem == null) continue;

                int slot = SlotPos.of(row, column).getSlot();
                inventory.setItem(slot, menuItem.getItemStack());

                if (!menuObject.isHasUpdatableItems() && menuItem.isUpdatable()) {
                    menuObject.setHasUpdatableItems(true);
                }
            }
        }
    }
}