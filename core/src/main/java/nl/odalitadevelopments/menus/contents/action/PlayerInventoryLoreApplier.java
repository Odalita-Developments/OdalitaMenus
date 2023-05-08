package nl.odalitadevelopments.menus.contents.action;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlayerInventoryLoreApplier {

    @NotNull ItemStack apply(@NotNull Integer slot, @NotNull ItemStack current);
}