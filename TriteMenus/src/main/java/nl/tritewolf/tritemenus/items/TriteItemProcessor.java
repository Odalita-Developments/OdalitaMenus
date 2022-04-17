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
            int position = 9 * (slotPos.getRow() - 1) + slotPos.getColumn();
            inventory.addItem(triteMenuItem.getItemStack());

            if()
        });
    }

}
