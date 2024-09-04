package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface MenuItemDataProvider {

    @NotNull
    ItemStack provideData(@NotNull AbstractMenuSession<?, ?, ?> session, @NotNull ItemStack itemStack);
}