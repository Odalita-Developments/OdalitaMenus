package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull Player player, @NotNull MenuContents menuContents);
}