package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import org.bukkit.entity.Player;

public abstract class TritePlayerMenuProvider extends TriteMenuProvider {

    public abstract void onLoad(Player player, TriteInventoryContents triteInventoryContents);
}