package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuProviderLoader<P extends MenuProvider> {

    static <P extends MenuProvider> MenuProviderLoader<P> defaultLoader() {
        return (provider, player, contents) -> {
            if (provider instanceof PlayerMenuProvider playerMenuProvider) {
                playerMenuProvider.onLoad(player, contents);
            } else if (provider instanceof GlobalMenuProvider globalMenuProvider) {
                globalMenuProvider.onLoad(contents);
            } else {
                throw new IllegalStateException("Unknown provider type: " + provider.getClass().getName());
            }
        };
    }

    void load(@NotNull P provider, @NotNull Player player, @NotNull InventoryContents contents);
}