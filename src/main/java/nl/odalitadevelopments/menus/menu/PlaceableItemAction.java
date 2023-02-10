package nl.odalitadevelopments.menus.menu;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface PlaceableItemAction {

    boolean shouldPlace(@NotNull SlotPos slotPos, @NotNull InventoryClickEvent event);
}