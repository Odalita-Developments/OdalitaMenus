package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import org.bukkit.inventory.Inventory;

public class TriteItemProcessor {

    public void initializeItems(TriteMenuObject triteMenuObject) {
        TriteMenuItem[][] contents = triteMenuObject.getContents();
        Inventory inventory = triteMenuObject.getInventory();

        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                TriteMenuItem triteMenuItem = contents[row][column];
                if (triteMenuItem == null) continue;

                int slot = 9 + row + column;
                inventory.setItem(slot, triteMenuItem.getItemStack());

                if (!triteMenuObject.isHasUpdatableItems() && triteMenuItem.isUpdatable()) {
                    triteMenuObject.setHasUpdatableItems(triteMenuObject.isHasUpdatableItems());
                }
            }
        }
    }
}
