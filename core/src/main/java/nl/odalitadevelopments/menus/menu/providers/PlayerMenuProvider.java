package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerMenuProvider extends MenuProvider {

    void onLoad(@NotNull Player player, @NotNull MenuContents menuContents);
}