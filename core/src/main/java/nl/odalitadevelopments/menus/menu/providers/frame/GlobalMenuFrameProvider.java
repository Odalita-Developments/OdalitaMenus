package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuContents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface GlobalMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull MenuContents menuContents);
}