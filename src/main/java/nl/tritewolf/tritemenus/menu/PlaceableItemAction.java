package nl.tritewolf.tritemenus.menu;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlaceableItemAction {

    boolean shouldPlace(@NotNull SlotPos slotPos, @Nullable ItemStack clickedItem, @Nullable ItemStack cursorItem);
}