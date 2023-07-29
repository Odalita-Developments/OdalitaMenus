package nl.odalitadevelopments.menus.listeners;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.utils.BukkitThreadHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public final class InventoryListener implements Listener {

    private final OdalitaMenus instance;
    private final MenuProcessor menuProcessor;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
        if (menuSession == null) return;

        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getClick() == ClickType.DOUBLE_CLICK) {
            event.setCancelled(true);
            return;
        }

        SupportedMenuType menuType = menuSession.getMenuType();
        Inventory clickedInventory = event.getClickedInventory();

        if (event.getView().getTopInventory().equals(menuSession.getInventory()) && event.getRawSlot() >= 0) {
            boolean clickedTopInventory = event.getView().getTopInventory().equals(clickedInventory);
            if (!clickedTopInventory && event.getCurrentItem() != null && menuSession.getCache().getPlayerInventoryClickAction() != null) {
                event.setCancelled(true); // Cancel event by default
                menuSession.getCache().getPlayerInventoryClickAction().accept(event);
                return;
            }

            List<Integer> placeableItems = menuSession.getCache().getPlaceableItems();
            if (event.getClick().isShiftClick() && !clickedTopInventory) {
                event.setCancelled(true);
                return;
            }

            if (!placeableItems.isEmpty() && !clickedTopInventory) return;
            if (placeableItems.contains(event.getRawSlot())) {
                PlaceableItemClickAction placeableItemAction = menuSession.getCache().getPlaceableItemClickAction();
                if (placeableItemAction != null && !placeableItemAction.shouldPlace(
                        SlotPos.of(menuType.maxRows(), menuType.maxColumns(), event.getRawSlot()),
                        event)) {
                    event.setCancelled(true);
                }

                return;
            }

            event.setCancelled(true);

            MenuItem menuItem = menuSession.getContent(SlotPos.of(menuType.maxRows(), menuType.maxColumns(), event.getRawSlot()));
            if (menuItem != null) {
                menuItem.onClick(this.instance).accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
        if (menuSession == null) return;

        SupportedMenuType menuType = menuSession.getMenuType();
        List<Integer> placeableItems = menuSession.getCache().getPlaceableItems();

        if (event.getView().getTopInventory().equals(menuSession.getInventory())) {
            Set<Integer> inventorySlots = event.getRawSlots();

            boolean fitsInMenu = event.getRawSlots().stream().allMatch(integer -> integer > (menuSession.getInventory().getSize() - 1));
            if (!placeableItems.isEmpty() && fitsInMenu) return;

            boolean matchAllSlots = new HashSet<>(placeableItems).containsAll(inventorySlots);
            if (!matchAllSlots) {
                event.setCancelled(true);
                return;
            }

            PlaceableItemDragAction placeableItemAction = menuSession.getCache().getPlaceableItemDragAction();
            if (placeableItemAction == null) {
                event.setCancelled(true);
                return;
            }

            List<SlotPos> slotPosses = new ArrayList<>();
            for (Integer slot : inventorySlots) {
                slotPosses.add(SlotPos.of(menuType.maxRows(), menuType.maxColumns(), slot));
            }

            if (!placeableItemAction.shouldPlace(slotPosses, event)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        MenuSession menuSession = this.menuProcessor.getOpenMenuSession(player);
        if (menuSession == null) return;

        Inventory inventory = event.getInventory();
        if (menuSession.getInventory().equals(inventory)) {
            Runnable closeActionBefore = menuSession.getCache().getCloseActionBefore();
            if (closeActionBefore != null) {
                closeActionBefore.run();
            }

            PlaceableItemsCloseAction placeableItemsCloseAction = menuSession.getCache().getPlaceableItemsCloseAction();
            if (placeableItemsCloseAction != null && placeableItemsCloseAction.equals(PlaceableItemsCloseAction.RETURN)) {
                List<Integer> placeableItems = menuSession.getCache().getPlaceableItems();

                placeableItems.forEach(integer -> {
                    ItemStack item = inventory.getItem(integer);
                    if (item != null) player.getInventory().addItem(item);
                });
            }

            Runnable closeActionAfter = menuSession.getCache().getCloseActionAfter();
            if (closeActionAfter != null) {
                closeActionAfter.run();
            }

            menuSession.setClosed(true);
            this.menuProcessor.getOpenMenus().remove(player);

            BukkitThreadHelper.runAsync(this.instance.getJavaPlugin(), player::updateInventory);
        }
    }
}