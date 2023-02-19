package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface PlayerMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull Player player, @NotNull MenuContents menuContents);
}