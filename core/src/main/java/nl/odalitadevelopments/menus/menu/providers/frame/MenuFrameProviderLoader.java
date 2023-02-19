package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuFrameProviderLoader<P extends MenuFrameProvider> {

    static <P extends MenuFrameProvider> MenuFrameProviderLoader<P> defaultLoader() {
        return (provider, player, contents) -> {
            if (provider instanceof PlayerMenuFrameProvider playerMenuFrameProvider) {
                playerMenuFrameProvider.onLoad(player, contents);
            } else if (provider instanceof GlobalMenuFrameProvider globalMenuFrameProvider) {
                globalMenuFrameProvider.onLoad(contents);
            } else {
                throw new IllegalStateException("Unknown provider type: " + provider.getClass().getName());
            }
        };
    }

    void load(@NotNull P provider, @NotNull Player player, @NotNull MenuContents contents);
}