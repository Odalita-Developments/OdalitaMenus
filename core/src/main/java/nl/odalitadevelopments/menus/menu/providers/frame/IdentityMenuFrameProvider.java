package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuFrameContents;
import nl.odalitadevelopments.menus.identity.Identity;
import org.jetbrains.annotations.NotNull;

public interface IdentityMenuFrameProvider<T> extends MenuFrameProvider {

    void onLoad(@NotNull Identity<T> identity, @NotNull MenuFrameContents menuContents);
}