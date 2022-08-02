package nl.tritewolf.testplugin.sb;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.providers.MenuProviderLoader;
import org.bukkit.entity.Player;

public interface TestExtraMenuProvider extends MenuProvider {

    static MenuProviderLoader<TestExtraMenuProvider> loader() {
        return (provider, player, contents) -> {
            provider.onLoad(player, new ExtraPlayer(), contents);
        };
    }

    void onLoad(Player player, ExtraPlayer extraPlayer, InventoryContents contents);
}