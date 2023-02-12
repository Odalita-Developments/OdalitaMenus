package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerMenuProvider extends MenuProvider {

    void onLoad(@NotNull Player player, @NotNull InventoryContents inventoryContents);
}