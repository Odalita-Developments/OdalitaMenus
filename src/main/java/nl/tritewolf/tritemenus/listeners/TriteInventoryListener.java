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
import org.bukkit.inventory.Inventory;

public final class TriteInventoryListener implements Listener {

    @TriteJect
    private TriteMenuProcessor menuProcessor;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        TriteMenuObject openMenuObject = this.menuProcessor.getOpenMenus().get(player.getUniqueId());
        if (openMenuObject == null) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (openMenuObject.getInventory().equals(clickedInventory)) {
            event.setCancelled(true);

            TriteMenuItem menuItem = openMenuObject.getContent(TriteSlotPos.of(event.getSlot()));
            if (menuItem != null) {
                menuItem.onClick().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        TriteMenuObject openMenuObject = this.menuProcessor.getOpenMenus().get(player.getUniqueId());
        if (openMenuObject == null) return;

        Inventory inventory = event.getInventory();
        if (openMenuObject.getInventory().equals(inventory)) {
            this.menuProcessor.getOpenMenus().remove(player.getUniqueId());
        }
    }
}