package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuFrameContents;
import nl.odalitadevelopments.menus.identity.Identity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuFrameProviderLoader<P extends MenuFrameProvider, T> {

    @SuppressWarnings("rawtypes, unchecked")
    static <P extends MenuFrameProvider> MenuFrameProviderLoader<P, ?> defaultLoader() {
        return (provider, data, contents) -> {
            if (provider instanceof PlayerMenuFrameProvider playerMenuFrameProvider) {
                playerMenuFrameProvider.onLoad((Player) data, contents);
            } else if (provider instanceof IdentityMenuFrameProvider identityMenuFrameProvider) {
                identityMenuFrameProvider.onLoad((Identity) data, contents);
            } else {
                throw new IllegalStateException("Unknown provider type: " + provider.getClass().getName());
            }
        };
    }

    void load(@NotNull P provider, @NotNull T data, @NotNull MenuFrameContents contents);
}