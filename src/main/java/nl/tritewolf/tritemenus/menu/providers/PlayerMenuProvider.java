package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public non-sealed interface PlayerMenuProvider extends MenuProvider {

    void onLoad(@NotNull Player player, @NotNull InventoryContents inventoryContents);
}