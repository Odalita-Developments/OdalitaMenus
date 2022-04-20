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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class MenuProcessor {

    @TriteJect
    @Getter(AccessLevel.NONE)
    private MenuContainer menuContainer;
    @TriteJect
    @Getter(AccessLevel.NONE)
    private ItemProcessor itemProcessor;

    private final Map<UUID, Map<Class<?>, MenuObject>> playerMenus = new HashMap<>();
    private final Map<Class<?>, MenuObject> globalMenus = new HashMap<>();

    private final Map<UUID, MenuObject> openMenus = new ConcurrentHashMap<>();

    public void openMenu(Class<? extends MenuProvider> clazz, Player player) {
        this.openMenuBuilder(clazz, player)
                .open();
    }

    public MenuOpener openMenuBuilder(Class<? extends MenuProvider> clazz, Player player) {
        Menu menuAnnotation = clazz.getAnnotation(Menu.class);
        return new MenuOpener(this, this.menuContainer, this.itemProcessor, player, clazz, menuAnnotation);
    }

    @AllArgsConstructor
    public static class MenuOpener {

        private final MenuProcessor processor;
        private final MenuContainer menuContainer;
        private final ItemProcessor itemProcessor;

        private final Player player;
        private final Class<? extends MenuProvider> providerClass;
        private final Menu annotation;
        private final List<Object> objects = new ArrayList<>();
        private final Map<String, Integer> paginationPages = new HashMap<>();

        public MenuOpener paginationPages(Map<String, Integer> paginationPages) {
            this.paginationPages.putAll(paginationPages);
            return this;
        }

        public MenuOpener pagination(String key, int page) {
            this.paginationPages.put(key, page);
            return this;
        }

        public MenuOpener objects(Object... objects) {
            this.objects.addAll(Arrays.asList(objects));
            return this;
        }

        public MenuOpener objects(List<Object> objects) {
            this.objects.addAll(objects);
            return this;
        }

        public MenuOpener object(Object object) {
            this.objects.add(object);
            return this;
        }

        public void open() {
            if (GlobalMenuProvider.class.isAssignableFrom(this.providerClass)) {
                this.openGlobalMenu(this.player, this.providerClass, this.annotation);
                return;
            }

            if (PlayerMenuProvider.class.isAssignableFrom(this.providerClass)) {
                this.openPlayerMenu(this.player, this.providerClass, this.annotation);
            }
        }

        private void openGlobalMenu(Player player, Class<? extends MenuProvider> clazz, Menu annotation) {
            MenuObject menuObject = this.processor.getGlobalMenus().get(clazz);
            if (menuObject != null) {
                this.openInventory(player, menuObject);
                return;
            }

            menuObject = new MenuObject(annotation.rows(), annotation.displayName());

            MenuProvider menuProvider = this.menuContainer.getMenuProviderByClass(clazz);
            if (menuProvider instanceof GlobalMenuProvider) {
                GlobalMenuProvider triteGlobalMenuProvider = (GlobalMenuProvider) menuProvider;

                triteGlobalMenuProvider.onLoad(new InventoryContents(menuObject));
                this.itemProcessor.initializeItems(menuObject);

                this.openInventory(player, menuObject);

                this.processor.getGlobalMenus().put(clazz, menuObject);
            }
        }

        private void openPlayerMenu(Player player, Class<? extends MenuProvider> clazz, Menu annotation) {
            Map<Class<?>, MenuObject> registeredPlayerMenus = this.processor.getPlayerMenus().get(player.getUniqueId());

            MenuObject menuObject;
            if (registeredPlayerMenus != null && !registeredPlayerMenus.isEmpty()) {
                menuObject = registeredPlayerMenus.get(clazz);
                this.openInventory(player, menuObject);
                return;
            }

            menuObject = new MenuObject(annotation.rows(), annotation.displayName());

            MenuProvider menuProvider = this.menuContainer.getMenuProviderByClass(clazz);
            if (menuProvider instanceof PlayerMenuProvider) {
                PlayerMenuProvider tritePlayerMenuProvider = (PlayerMenuProvider) menuProvider;

                tritePlayerMenuProvider.onLoad(player, new InventoryContents(menuObject));
                this.itemProcessor.initializeItems(menuObject);

                this.openInventory(player, menuObject);

                this.processor.getPlayerMenus().computeIfAbsent(player.getUniqueId(), (uuid) -> new HashMap<>())
                        .put(clazz, menuObject);
            }
        }

        private void openInventory(Player player, MenuObject menuObject) {
            player.openInventory(menuObject.getInventory());
            this.processor.getOpenMenus().put(player.getUniqueId(), menuObject);
        }
    }
}