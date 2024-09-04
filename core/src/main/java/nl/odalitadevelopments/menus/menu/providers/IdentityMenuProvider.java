package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.MenuIdentityContents;
import org.jetbrains.annotations.NotNull;

public interface IdentityMenuProvider<T> extends MenuProvider {

    void onLoad(@NotNull MenuIdentityContents<T> menuIdentityContents);
}