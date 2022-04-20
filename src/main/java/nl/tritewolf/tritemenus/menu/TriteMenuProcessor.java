package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.items.TriteItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.TritePlayerMenuProvider;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class TriteMenuProcessor {

    @TriteJect
    @Getter(AccessLevel.NONE)
    private TriteMenuContainer triteMenuContainer;
    @TriteJect
    @Getter(AccessLevel.NONE)
    private TriteItemProcessor triteItemProcessor;

    private final Map<UUID, Map<Class<?>, TriteMenuObject>> playerMenus = new HashMap<>();
    private final Map<Class<?>, TriteMenuObject> globalMenus = new HashMap<>();

    private final Map<UUID, TriteMenuObject> openMenus = new ConcurrentHashMap<>();

    public void openMenu(Class<? extends TriteMenuProvider> clazz, Player player) {
        if (!clazz.isAnnotationPresent(TriteMenu.class)) {
            //todo throw exception
            return;
        }

        TriteMenu menuAnnotation = clazz.getAnnotation(TriteMenu.class);

        if (TriteGlobalMenuProvider.class.isAssignableFrom(clazz)) {
            this.openGlobalMenu(player, clazz, menuAnnotation);
            return;
        }

        if (TritePlayerMenuProvider.class.isAssignableFrom(clazz)) {
            this.openPlayerMenu(player, clazz, menuAnnotation);
        }
    }

    private void openGlobalMenu(Player player, Class<? extends TriteMenuProvider> clazz, TriteMenu annotation) {
        TriteMenuObject menuObject = this.globalMenus.get(clazz);
        if (menuObject != null) {
            this.openInventory(player, menuObject);
            return;
        }

        menuObject = new TriteMenuObject(annotation.rows(), annotation.displayName());

        TriteMenuProvider triteMenuProvider = this.triteMenuContainer.getMenuProviderByClass(clazz);
        if (triteMenuProvider instanceof TriteGlobalMenuProvider) {
            TriteGlobalMenuProvider triteGlobalMenuProvider = (TriteGlobalMenuProvider) triteMenuProvider;

            triteGlobalMenuProvider.onLoad(new TriteInventoryContents(menuObject));
            this.triteItemProcessor.initializeItems(menuObject);

            this.openInventory(player, menuObject);

            this.globalMenus.put(clazz, menuObject);
        }
    }

    private void openPlayerMenu(Player player, Class<? extends TriteMenuProvider> clazz, TriteMenu annotation) {
        Map<Class<?>, TriteMenuObject> registeredPlayerMenus = this.playerMenus.get(player.getUniqueId());

        TriteMenuObject menuObject;
        if (registeredPlayerMenus != null && !registeredPlayerMenus.isEmpty()) {
            menuObject = registeredPlayerMenus.get(clazz);
            this.openInventory(player, menuObject);
            return;
        }

        menuObject = new TriteMenuObject(annotation.rows(), annotation.displayName());

        TriteMenuProvider triteMenuProvider = this.triteMenuContainer.getMenuProviderByClass(clazz);
        if (triteMenuProvider instanceof TritePlayerMenuProvider) {
            TritePlayerMenuProvider tritePlayerMenuProvider = (TritePlayerMenuProvider) triteMenuProvider;

            tritePlayerMenuProvider.onLoad(player, new TriteInventoryContents(menuObject));
            this.triteItemProcessor.initializeItems(menuObject);

            this.openInventory(player, menuObject);

            this.playerMenus.computeIfAbsent(player.getUniqueId(), (uuid) -> new HashMap<>())
                    .put(clazz, menuObject);
        }
    }

    private void openInventory(Player player, TriteMenuObject menuObject) {
        player.openInventory(menuObject.getInventory());
        this.openMenus.put(player.getUniqueId(), menuObject);
    }
}