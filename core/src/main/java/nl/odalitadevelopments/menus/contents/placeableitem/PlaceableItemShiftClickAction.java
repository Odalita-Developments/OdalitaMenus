package nl.odalitadevelopments.menus.contents.placeableitem;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface PlaceableItemShiftClickAction {

    boolean shouldPlace(@NotNull List<SlotPos> slotPosses, @NotNull ItemStack added, @NotNull InventoryClickEvent event);
}