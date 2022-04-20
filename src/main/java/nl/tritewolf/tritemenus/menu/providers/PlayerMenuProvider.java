package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import org.bukkit.entity.Player;

public interface PlayerMenuProvider extends MenuProvider {

    void onLoad(Player player, InventoryContents inventoryContents);
}