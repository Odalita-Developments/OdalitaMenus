package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.menu.MenuObject;
import org.bukkit.inventory.Inventory;

public final class ItemProcessor {

    public void initializeItems(MenuObject menuObject) {
        MenuItem[][] contents = menuObject.getContents();
        Inventory inventory = menuObject.getInventory();

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