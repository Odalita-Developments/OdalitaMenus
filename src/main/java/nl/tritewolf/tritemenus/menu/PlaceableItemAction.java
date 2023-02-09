package nl.tritewolf.tritemenus.menu;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface PlaceableItemAction {

    boolean shouldPlace(@NotNull SlotPos slotPos, @NotNull InventoryClickEvent event);
}