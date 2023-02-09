package nl.tritewolf.tritemenus.listeners;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.menu.PlaceableItemAction;
import nl.tritewolf.tritemenus.menu.PlaceableItemsCloseAction;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record InventoryListener(TriteMenus instance, MenuProcessor menuProcessor) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuSession openMenuSession = this.menuProcessor.getOpenMenus().get(player);
        if (openMenuSession == null) return;

        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getClick() == ClickType.DOUBLE_CLICK) {
            event.setCancelled(true);
            return;
        }

        MenuType menuType = openMenuSession.getMenuType();
        Inventory clickedInventory = event.getClickedInventory();

        if (event.getView().getTopInventory().equals(openMenuSession.getInventory()) && event.getRawSlot() >= 0) {
            boolean clickedTopInventory = event.getView().getTopInventory().equals(clickedInventory);
            if (!clickedTopInventory && event.getCurrentItem() != null && openMenuSession.getCache().getPlayerInventoryClickAction() != null) {
                event.setCancelled(true); // Cancel event by default
                openMenuSession.getCache().getPlayerInventoryClickAction().accept(event);
                return;
            }

            List<Integer> placeableItems = openMenuSession.getCache().getPlaceableItems();
            if (event.getClick().isShiftClick() && !clickedTopInventory) {
                event.setCancelled(true);
                return;
            }

            if (!placeableItems.isEmpty() && !clickedTopInventory) return;
            if (placeableItems.contains(event.getRawSlot())) {
                PlaceableItemAction placeableItemAction = openMenuSession.getCache().getPlaceableItemAction();
                if (placeableItemAction != null && !placeableItemAction.shouldPlace(
                        SlotPos.of(menuType.maxRows(), menuType.maxColumns(), event.getRawSlot()),
                        event)) {
                    event.setCancelled(true);
                }

                return;
            }

            event.setCancelled(true);

            MenuItem menuItem = openMenuSession.getContent(SlotPos.of(menuType.maxRows(), menuType.maxColumns(), event.getRawSlot()));
            if (menuItem != null) {
                menuItem.onClick(this.instance).accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuSession openMenuSession = this.menuProcessor.getOpenMenus().get(player);
        if (openMenuSession == null) return;

        List<Integer> placeableItems = openMenuSession.getCache().getPlaceableItems();

        if (event.getView().getTopInventory().equals(openMenuSession.getInventory())) {
            Set<Integer> inventorySlots = event.getRawSlots();

            boolean fitsInMenu = event.getRawSlots().stream().allMatch(integer -> integer > (openMenuSession.getInventory().getSize() - 1));
            if (!placeableItems.isEmpty() && fitsInMenu) return;

            boolean matchAllSlots = new HashSet<>(placeableItems).containsAll(inventorySlots);
            if (!matchAllSlots) {
                event.setCancelled(true);
                return;
            }

            PlaceableItemAction placeableItemAction = openMenuSession.getCache().getPlaceableItemAction();
            if (placeableItemAction != null) {
                for (Integer slot : inventorySlots) {
                    if (!placeableItemAction.shouldPlace(
                            SlotPos.of(openMenuSession.getMenuType().maxRows(), openMenuSession.getMenuType().maxColumns(), slot),
                            new InventoryClickEvent(event.getView(), InventoryType.SlotType.CONTAINER, slot, ClickType.UNKNOWN, InventoryAction.UNKNOWN))) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        MenuSession openMenuSession = this.menuProcessor.getOpenMenus().get(player);
        if (openMenuSession == null) return;

        Inventory inventory = event.getInventory();
        if (openMenuSession.getInventory().equals(inventory)) {
            PlaceableItemsCloseAction placeableItemsCloseAction = openMenuSession.getCache().getPlaceableItemsCloseAction();

            if (placeableItemsCloseAction != null && placeableItemsCloseAction.equals(PlaceableItemsCloseAction.RETURN)) {
                List<Integer> placeableItems = openMenuSession.getCache().getPlaceableItems();

                placeableItems.forEach(integer -> {
                    ItemStack item = inventory.getItem(integer);
                    if (item != null) player.getInventory().addItem(item);
                });
            }

            this.menuProcessor.getOpenMenus().remove(player);
        }
    }
}