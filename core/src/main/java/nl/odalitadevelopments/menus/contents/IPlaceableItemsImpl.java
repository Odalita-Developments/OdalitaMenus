package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.interfaces.IPlaceableItemContents;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

interface IPlaceableItemsImpl extends IPlaceableItemContents {

    AbstractMenuSession<?, ?, ?> menuSession();

    @NotNull
    MenuSessionCache cache();

    @Override
    default void registerPlaceableItemSlots(int... slots) {
        for (int slot : slots) {
            SlotPos slotPos = SlotPos.of(slot);
            if (!this.menuSession().fits(slotPos)) continue;

            this.cache().getPlaceableItems().add(slot);
        }
    }

    @Override
    default void allowPlaceableItemShiftClick(boolean allowShiftClick) {
        this.cache().setAllowPlaceableItemShiftClick(allowShiftClick);
    }

    @Override
    default void allowPlaceableItemDrag(boolean allowDrag) {
        this.cache().setAllowPlaceableItemDrag(allowDrag);
    }

    @Override
    default void setForcedPlaceableItem(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        if (!this.menuSession().fits(slotPos)) return;

        this.menuSession().inventory().setItem(slotPos.getSlot(), itemStack);
    }

    @Override
    default void setForcedPlaceableItem(int row, int column, @NotNull ItemStack itemStack) {
        this.setForcedPlaceableItem(SlotPos.of(row, column), itemStack);
    }

    @Override
    default void setForcedPlaceableItem(int slot, @NotNull ItemStack itemStack) {
        this.setForcedPlaceableItem(SlotPos.of(slot), itemStack);
    }

    @Override
    default void placeableItemsCloseAction(@NotNull PlaceableItemsCloseAction action) {
        this.cache().setPlaceableItemsCloseAction(action);
    }

    @Override
    default @NotNull Optional<@NotNull SlotPos> firstEmptyPlaceableItemSlot() {
        MenuSessionCache cache = this.cache();
        if (cache.getPlaceableItems().isEmpty()) return Optional.empty();

        for (int slot : cache.getPlaceableItems()) {
            ItemStack item = this.menuSession().inventory().getItem(slot);
            if (item == null || item.getType().isAir()) {
                return Optional.of(SlotPos.of(slot));
            }
        }

        return Optional.empty();
    }

    @Override
    default @NotNull Map<Integer, ItemStack> getPlaceableItems() {
        Map<Integer, ItemStack> items = new HashMap<>();
        MenuSessionCache cache = this.cache();
        if (cache.getPlaceableItems().isEmpty()) return items;

        for (int slot : cache.getPlaceableItems()) {
            ItemStack item = this.menuSession().inventory().getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            items.put(slot, item);
        }

        return items;
    }

    @Override
    default void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action) {
        this.cache().setPlaceableItemsCloseAction(action);
        player.closeInventory();
    }
}