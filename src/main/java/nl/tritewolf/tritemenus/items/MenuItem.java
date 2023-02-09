package nl.tritewolf.tritemenus.items;

import nl.tritewolf.tritemenus.TriteMenus;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface MenuItem {

    @NotNull
    ItemStack getItemStack(@NotNull TriteMenus instance);

    @NotNull
    Consumer<InventoryClickEvent> onClick(@NotNull TriteMenus instance);

    default boolean isUpdatable() {
        return false;
    }

    default int getUpdateTicks() {
        return -1;
    }
}