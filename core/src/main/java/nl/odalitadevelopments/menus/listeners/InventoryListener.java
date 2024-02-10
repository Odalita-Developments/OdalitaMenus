package nl.odalitadevelopments.menus.listeners;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
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

import java.util.*;

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

        ItemStack currentItem = event.getCurrentItem();

        SupportedMenuType menuType = menuSession.getMenuType();
        Inventory clickedInventory = event.getClickedInventory();

        if (event.getView().getTopInventory().equals(menuSession.getInventory()) && event.getRawSlot() >= 0) {
            boolean clickedTopInventory = event.getView().getTopInventory().equals(clickedInventory);
            if (!clickedTopInventory && currentItem != null && menuSession.getCache().getPlayerInventoryClickAction() != null) {
                event.setCancelled(true); // Cancel event by default
                menuSession.getCache().getPlayerInventoryClickAction().accept(event);
                return;
            }

            // Cancel if the player uses shift click from top inventory when shift click is not allowed
            if (event.getClick().isShiftClick() && clickedTopInventory && !menuSession.getCache().isAllowPlaceableItemShiftClick()) {
                event.setCancelled(true);
                return;
            }

            // Handle shift click for placeable items from bottom inventory
            List<Integer> placeableItemsSlots = menuSession.getCache().getPlaceableItems();
            if (event.getClick().isShiftClick() && !clickedTopInventory) {
                event.setCancelled(true);

                if (currentItem == null || placeableItemsSlots.isEmpty() || !menuSession.getCache().isAllowPlaceableItemShiftClick()) {
                    return;
                }

                ItemStack currentItemClone = currentItem.clone();
                Map<Integer, ItemStack> slotsModified = new HashMap<>();
                NavigableMap<Integer, ItemStack> placeableItems = this.getPlaceableItems(menuSession.getInventory(), placeableItemsSlots);

                // Check if the item is already present in one of the placeable item slots
                for (Map.Entry<Integer, ItemStack> entry : placeableItems.entrySet()) {
                    int slot = entry.getKey();
                    ItemStack item = entry.getValue();

                    if (item != null && !item.getType().isAir() && item.isSimilar(currentItem)) {
                        int maxStackSize = item.getMaxStackSize();
                        int amount = item.getAmount();
                        int amountToAdd = currentItemClone.getAmount();

                        item = item.clone();

                        if (amount + amountToAdd > maxStackSize) {
                            int amountLeft = maxStackSize - amount;
                            item.setAmount(maxStackSize);
                            currentItemClone.setAmount(amountToAdd - amountLeft);

                            slotsModified.put(slot, item);
                        } else {
                            item.setAmount(amount + amountToAdd);
                            currentItemClone = null;

                            slotsModified.put(slot, item);
                            break;
                        }
                    }
                }

                // Place the item in the first empty placeable item slot if it is not fully placed yet
                if (currentItemClone != null) {
                    for (Map.Entry<Integer, ItemStack> entry : placeableItems.entrySet()) {
                        int slot = entry.getKey();
                        ItemStack item = entry.getValue();

                        // If the slot is empty, place the item
                        if (item == null || item.getType().isAir()) {
                            slotsModified.put(slot, currentItemClone);
                            currentItemClone = null;
                            break;
                        }
                    }
                }

                if (!slotsModified.isEmpty()) {
                    PlaceableItemShiftClickAction action = menuSession.getCache().getPlaceableItemShiftClickAction();
                    boolean shouldPlace = true;
                    if (action != null) {
                        List<SlotPos> slotPosses = new ArrayList<>();
                        for (Integer slot : slotsModified.keySet()) {
                            slotPosses.add(SlotPos.of(menuType.maxRows(), menuType.maxColumns(), slot));
                        }

                        shouldPlace = action.shouldPlace(slotPosses, currentItem, event);
                    }

                    if (shouldPlace) {
                        for (Map.Entry<Integer, ItemStack> entry : slotsModified.entrySet()) {
                            event.getView().getTopInventory().setItem(entry.getKey(), entry.getValue());
                            event.setCurrentItem(currentItemClone);
                        }
                    }
                }

                return;
            }

            if (!placeableItemsSlots.isEmpty() && !clickedTopInventory) return;
            if (placeableItemsSlots.contains(event.getRawSlot())) {
                PlaceableItemClickAction action = menuSession.getCache().getPlaceableItemClickAction();
                if (action != null && !action.shouldPlace(
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

        if (!menuSession.getCache().isAllowPlaceableItemDrag()) {
            event.setCancelled(true);
            return;
        }

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

            List<SlotPos> slotPosses = new ArrayList<>();
            for (Integer slot : inventorySlots) {
                slotPosses.add(SlotPos.of(menuType.maxRows(), menuType.maxColumns(), slot));
            }

            PlaceableItemDragAction action = menuSession.getCache().getPlaceableItemDragAction();
            if (action != null && !action.shouldPlace(slotPosses, event)) {
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
            for (OdalitaEventListener eventListener : menuSession.getCache().getEventListeners()) {
                eventListener.unregister();
            }

            Runnable closeActionBefore = menuSession.getCache().getCloseActionBefore();
            if (closeActionBefore != null) {
                closeActionBefore.run();
            }

            PlaceableItemsCloseAction action = menuSession.getCache().getPlaceableItemsCloseAction();
            if (action != null && action.equals(PlaceableItemsCloseAction.RETURN)) {
                List<Integer> placeableItemsSlots = menuSession.getCache().getPlaceableItems();
                NavigableMap<Integer, ItemStack> placeableItems = this.getPlaceableItems(inventory, placeableItemsSlots);
                for (ItemStack item : placeableItems.values()) {
                    if (item != null && !item.getType().isAir()) {
                        HashMap<Integer, ItemStack> rest = player.getInventory().addItem(item);
                        if (!rest.isEmpty()) {
                            rest.values().forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
                        }
                    }
                }
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

    private NavigableMap<Integer, ItemStack> getPlaceableItems(Inventory inventory, List<Integer> placeableItemSlots) {
        NavigableMap<Integer, ItemStack> placeableItems = new TreeMap<>();

        for (Integer placeableItemSlot : placeableItemSlots) {
            ItemStack item = inventory.getItem(placeableItemSlot);
            placeableItems.put(placeableItemSlot, item);
        }

        return placeableItems;
    }
}