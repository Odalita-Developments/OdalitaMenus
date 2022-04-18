package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class TriteItemProcessor {

    public void initializeItems(TriteMenuObject triteMenuObject) {
        Map<TriteSlotPos, TriteMenuItem> inventoryContents = triteMenuObject.getContents();
        Inventory inventory = triteMenuObject.getInventory();

        inventoryContents.forEach((slotPos, triteMenuItem) -> {
            inventory.setItem(slotPos.getSlot(), triteMenuItem.getItemStack());

            if (!triteMenuObject.isHasUpdatableItems() && triteMenuItem.isUpdatable()) {
                triteMenuObject.setHasUpdatableItems(triteMenuObject.isHasUpdatableItems());
            }
        });

    }

}
