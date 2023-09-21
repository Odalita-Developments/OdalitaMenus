package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuFrameContents;
import org.jetbrains.annotations.NotNull;

public interface GlobalMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull MenuFrameContents menuContents);
}