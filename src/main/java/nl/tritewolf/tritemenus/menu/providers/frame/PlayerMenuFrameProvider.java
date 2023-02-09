package nl.tritewolf.tritemenus.menu.providers.frame;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface PlayerMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull Player player, @NotNull InventoryContents inventoryContents);
}