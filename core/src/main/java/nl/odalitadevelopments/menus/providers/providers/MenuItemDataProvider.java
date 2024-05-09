package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.menu.MenuSession;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface MenuItemDataProvider {

    @NotNull
    ItemStack provideData(@NotNull MenuSession session, @NotNull ItemStack itemStack);
}