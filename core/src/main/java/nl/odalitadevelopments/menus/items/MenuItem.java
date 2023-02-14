package nl.odalitadevelopments.menus.items;

import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class MenuItem {



    public abstract @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance);

    public abstract @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance);

    public boolean isUpdatable() {
        return false;
    }

    public int getUpdateTicks() {
        return -1;
    }
}