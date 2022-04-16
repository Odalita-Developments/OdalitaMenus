package nl.tritewolf.tritemenus.items;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface TriteMenuItem {

    ItemStack getItemStack();

    Consumer<InventoryClickEvent> onClick();

    default boolean isUpdatable() {
        return false;
    }

    default int getUpdateTicks() {
        return -1;
    }
}
