package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import org.bukkit.entity.Player;

public interface TritePlayerMenuProvider extends TriteMenuProvider {

    void onLoad(Player player, TriteInventoryContents triteInventoryContents);
}