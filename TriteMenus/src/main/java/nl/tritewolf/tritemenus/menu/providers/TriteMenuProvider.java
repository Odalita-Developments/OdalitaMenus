package nl.tritewolf.tritemenus.menu.providers;

import lombok.Setter;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import org.bukkit.entity.Player;

public interface TriteMenuProvider {


    void onLoad(Player player, TriteInventoryContents triteInventoryContents);

}
