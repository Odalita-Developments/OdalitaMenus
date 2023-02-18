package nl.odalitadevelopments.menus.contents.placeableitem;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlaceableItemClickAction {

    boolean shouldPlace(@NotNull SlotPos slotPos, @NotNull InventoryClickEvent event);
}