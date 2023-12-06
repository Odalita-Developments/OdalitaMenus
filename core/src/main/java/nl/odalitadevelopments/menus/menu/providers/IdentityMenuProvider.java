package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.MenuIdentityContents;
import org.jetbrains.annotations.NotNull;

public interface IdentityMenuProvider extends MenuProvider {

    void onLoad(@NotNull MenuIdentityContents menuIdentityContents);
}