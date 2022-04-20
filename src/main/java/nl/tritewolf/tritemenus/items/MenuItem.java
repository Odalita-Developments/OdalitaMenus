package nl.tritewolf.tritemenus.items;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface MenuItem {

    @NotNull
    ItemStack getItemStack();

    @NotNull
    Consumer<InventoryClickEvent> onClick();

    default boolean isUpdatable() {
        return false;
    }

    default int getUpdateTicks() {
        return -1;
    }
}