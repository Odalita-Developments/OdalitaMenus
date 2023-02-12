package nl.odalitadevelopments.menus.items;

import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface MenuItem {

    @NotNull
    ItemStack getItemStack(@NotNull OdalitaMenus instance);

    @NotNull
    Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance);

    default boolean isUpdatable() {
        return false;
    }

    default int getUpdateTicks() {
        return -1;
    }
}