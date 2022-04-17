package nl.tritewolf.tritemenus.listeners;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.menu.TriteMenuContainer;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Map;

public class TriteGlobalInventoryListener implements Listener {

    @TriteJect
    private TriteMenuContainer triteMenuContainer;

    @EventHandler
    public void onCLick(InventoryClickEvent event) {
        Map<Class<?>, Pair<TriteGlobalMenuProvider, TriteMenuObject>> triteGlobalMenus = triteMenuContainer.getTriteGlobalMenus();
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
