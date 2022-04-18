package nl.tritewolf.tritemenus.listeners;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class TritePlayerInventoryListener implements Listener {

    @TriteJect
    private TriteMenuProcessor menuProcessor;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCLick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Map<UUID, Map<Class<?>, TriteMenuObject>> menus = menuProcessor.getMenus();

        Map<Class<?>, TriteMenuObject> triteMenuObjects = menus.get(player.getUniqueId());
        TriteMenuObject triteMenuObject = triteMenuObjects.values().stream().filter(TriteMenuObject::isHasMenuOpened).findFirst().orElse(null);
        Inventory clickedInventory = event.getClickedInventory();

        if (triteMenuObject != null && clickedInventory != null && clickedInventory.equals(triteMenuObject.getInventory())) {
            event.setCancelled(true);

            TriteMenuItem triteMenuItem = triteMenuObject.getContent(TriteSlotPos.of(event.getSlot()));
            if (triteMenuItem != null) {
                triteMenuItem.onClick().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(InventoryDragEvent event) {

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Map<UUID, Map<Class<?>, TriteMenuObject>> menus = menuProcessor.getMenus();

        Map<Class<?>, TriteMenuObject> triteMenuObjects = menus.get(player.getUniqueId());
        TriteMenuObject triteMenuObject = triteMenuObjects.values().stream().filter(TriteMenuObject::isHasMenuOpened).findFirst().orElse(null);
        Inventory inventory = event.getInventory();

        if (triteMenuObject != null && inventory.equals(triteMenuObject.getInventory())) {
            triteMenuObject.setHasMenuOpened(false);
        }
    }
}
