package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.GlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
        if (!clazz.isAnnotationPresent(Menu.class)) {
            //todo throw exception
            return;
        }

        Menu menuAnnotation = clazz.getAnnotation(Menu.class);

        if (GlobalMenuProvider.class.isAssignableFrom(clazz)) {
            this.openGlobalMenu(player, clazz, menuAnnotation);
            return;
        }

        if (PlayerMenuProvider.class.isAssignableFrom(clazz)) {
            this.openPlayerMenu(player, clazz, menuAnnotation);
        }
    }

    private void openGlobalMenu(Player player, Class<? extends MenuProvider> clazz, Menu annotation) {
        MenuObject menuObject = this.globalMenus.get(clazz);
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

            this.globalMenus.put(clazz, menuObject);
        }
    }

    private void openPlayerMenu(Player player, Class<? extends MenuProvider> clazz, Menu annotation) {
        Map<Class<?>, MenuObject> registeredPlayerMenus = this.playerMenus.get(player.getUniqueId());

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

            this.playerMenus.computeIfAbsent(player.getUniqueId(), (uuid) -> new HashMap<>())
                    .put(clazz, menuObject);
        }
    }

    private void openInventory(Player player, MenuObject menuObject) {
        player.openInventory(menuObject.getInventory());
        this.openMenus.put(player.getUniqueId(), menuObject);
    }
}