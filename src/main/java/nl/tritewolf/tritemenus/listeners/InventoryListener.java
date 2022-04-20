package nl.tritewolf.tritemenus.listeners;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class InventoryListener implements Listener {

    @TriteJect
    private MenuProcessor menuProcessor;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuObject openMenuObject = this.menuProcessor.getOpenMenus().get(player.getUniqueId());
        if (openMenuObject == null) return;

        Inventory clickedInventory = event.getClickedInventory();

        if (event.getView().getTopInventory().equals(openMenuObject.getInventory())) {
            List<Integer> placeableItems = openMenuObject.getPlaceableItems();
            if (!placeableItems.isEmpty() && event.getView().getBottomInventory().equals(clickedInventory))
                return;

            if (event.getClick().isShiftClick() && clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
                event.setCancelled(true);
                return;
            }

            if (placeableItems.contains(event.getSlot())) return;

            event.setCancelled(true);

            MenuItem menuItem = openMenuObject.getContent(SlotPos.of(event.getSlot()));
            if (menuItem != null) {
                menuItem.onClick().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuObject openMenuObject = this.menuProcessor.getOpenMenus().get(player.getUniqueId());
        if (event.getView().getTopInventory().equals(openMenuObject.getInventory())) {
            List<Integer> placeableItems = openMenuObject.getPlaceableItems();
            Set<Integer> inventorySlots = event.getInventorySlots();
            boolean matchAllSlots = new HashSet<>(placeableItems).containsAll(inventorySlots);

            if (!matchAllSlots) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        MenuObject openMenuObject = this.menuProcessor.getOpenMenus().get(player.getUniqueId());
        if (openMenuObject == null) return;

        Inventory inventory = event.getInventory();
        if (openMenuObject.getInventory().equals(inventory)) {
            this.menuProcessor.getOpenMenus().remove(player.getUniqueId());
        }
    }
}