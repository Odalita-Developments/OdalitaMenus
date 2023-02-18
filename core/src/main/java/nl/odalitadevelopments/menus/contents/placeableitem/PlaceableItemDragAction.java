package nl.odalitadevelopments.menus.contents.placeableitem;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface PlaceableItemDragAction {

    boolean shouldPlace(@NotNull List<SlotPos> slotPosses, @NotNull InventoryDragEvent event);
}