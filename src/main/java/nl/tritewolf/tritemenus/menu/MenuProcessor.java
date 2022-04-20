package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.GlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class MenuProcessor {

    @TriteJect
    @Getter(AccessLevel.NONE)
    private ItemProcessor itemProcessor;

    private final Map<UUID, MenuObject> openMenus = new ConcurrentHashMap<>();

    public void openMenu(MenuProvider menuProvider, Player player) {
        this.openMenuBuilder(menuProvider, player)
                .open();
    }

    @Contract("_, _ -> new")
    public @NotNull MenuOpener openMenuBuilder(MenuProvider menuProvider, Player player) {
        return new MenuOpener(this, this.itemProcessor, player, menuProvider);
    }

    @AllArgsConstructor
    public static class MenuOpener {

        private final MenuProcessor processor;
        private final ItemProcessor itemProcessor;

        private final Player player;
        private final MenuProvider menuProvider;
        private final Map<String, Integer> paginationPages = new HashMap<>();

        public MenuOpener paginationPages(Map<String, Integer> paginationPages) {
            this.paginationPages.putAll(paginationPages);
            return this;
        }

        public MenuOpener pagination(String key, int page) {
            this.paginationPages.put(key, page);
            return this;
        }

        public void open() {
            if (this.menuProvider instanceof GlobalMenuProvider) {
                this.openGlobalMenu(this.player, (GlobalMenuProvider) this.menuProvider);
                return;
            }

            if (this.menuProvider instanceof PlayerMenuProvider) {
                this.openPlayerMenu(this.player, (PlayerMenuProvider) this.menuProvider);
            }
        }

        private void openGlobalMenu(Player player, GlobalMenuProvider menuProvider) {
            try {
                Menu annotation = menuProvider.getClass().getAnnotation(Menu.class);
                MenuObject menuObject = new MenuObject(annotation.rows(), annotation.displayName());

                menuProvider.onLoad(new InventoryContents(menuObject));
                this.itemProcessor.initializeItems(menuObject);

                this.openInventory(player, menuObject);
            } catch (Exception exception) {
                exception.printStackTrace();
                // TODO idk, something else
            }
        }

        private void openPlayerMenu(Player player, PlayerMenuProvider menuProvider) {
            try {
                Menu annotation = menuProvider.getClass().getAnnotation(Menu.class);
                MenuObject menuObject = new MenuObject(annotation.rows(), annotation.displayName());

                menuProvider.onLoad(player, new InventoryContents(menuObject));
                this.itemProcessor.initializeItems(menuObject);

                this.openInventory(player, menuObject);
            } catch (Exception exception) {
                exception.printStackTrace();
                // TODO idk, something else
            }
        }

        private void openInventory(@NotNull Player player, @NotNull MenuObject menuObject) {
            player.openInventory(menuObject.getInventory());
            this.processor.getOpenMenus().put(player.getUniqueId(), menuObject);
        }
    }
}