package nl.tritewolf.tritemenus.listeners;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    public void onCLick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Map<UUID, TriteMenuObject> menus = menuProcessor.getMenus();

        TriteMenuObject triteMenuObject = menus.get(player.getUniqueId());
        Inventory clickedInventory = event.getClickedInventory();

        if (triteMenuObject != null && clickedInventory != null && clickedInventory.equals(triteMenuObject.getInventory())) {
            
        }
    }

    @EventHandler
    public void on(InventoryDragEvent event) {

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

    }
}
