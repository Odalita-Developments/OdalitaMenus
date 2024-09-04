package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.MenuIdentityContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuProviderLoader<P extends MenuProvider> {

    @SuppressWarnings("rawtypes, unchecked")
    static <P extends MenuProvider> MenuProviderLoader<P> defaultLoader() {
        return (provider, player, contents) -> {
            if (provider instanceof PlayerMenuProvider playerMenuProvider) {
                if (!(contents instanceof MenuContents menuContents)) {
                    throw new IllegalStateException("Menu contents must be of type MenuContents");
                }

                playerMenuProvider.onLoad(player, menuContents);
            } else if (provider instanceof IdentityMenuProvider identityMenuProvider) {
                if (!(contents instanceof MenuIdentityContents menuIdentityContents)) {
                    throw new IllegalStateException("Menu contents must be of type MenuIdentityContents");
                }

                identityMenuProvider.onLoad(menuIdentityContents);
            } else {
                throw new IllegalStateException("Unknown provider type: " + provider.getClass().getName());
            }
        };
    }

    void load(@NotNull P provider, @NotNull Player player, @NotNull IMenuContents contents);
}