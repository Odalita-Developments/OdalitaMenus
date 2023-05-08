package nl.odalitadevelopments.menus.contents.action;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlayerInventoryLoreApplier {

    void apply(@NotNull Integer slot, @NotNull ItemStack current);
}