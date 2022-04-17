package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.items.TriteItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TriteMenuProcessor {

    @TriteJect
    private TriteMenuContainer triteMenuContainer;
    @TriteJect
    private TriteItemProcessor triteItemProcessor;

    @Getter
    private final Map<UUID, TriteMenuObject> menus = new HashMap<>();
    @Getter
    private final Map<Class<?>, TriteMenuObject> globalMenus = new HashMap<>();

    public void openMenu(Class<?> clazz, Player player) {
        if (!clazz.isAnnotationPresent(TriteMenu.class)) {
            //todo throw exception
            return;
        }

        TriteMenu clazzAnnotation = clazz.getAnnotation(TriteMenu.class);
        if (clazzAnnotation.menuType().equals(TriteMenuType.GLOBAL)) {
            TriteMenuObject menuObject = globalMenus.get(clazz);
            if (menuObject != null) {
                player.openInventory(menuObject.getInventory());
                return;
            }

            Pair<TriteGlobalMenuProvider, TriteMenuObject> globalMenuPair = triteMenuContainer.getTriteGlobalMenus().get(clazz);
            TriteGlobalMenuProvider key = globalMenuPair.getKey();
            key.onLoad(new TriteInventoryContents(globalMenuPair.getValue()));
            triteItemProcessor.initializeItems(globalMenuPair.getValue());

            player.openInventory(globalMenuPair.getValue().getInventory());
            globalMenus.putIfAbsent(clazz, globalMenuPair.getValue());
            return;
        }

        TriteMenuObject menuObject = menus.get(player.getUniqueId());
        if (menuObject != null) {
            player.openInventory(menuObject.getInventory());
            menuObject.setHasMenuOpened(true);
            return;
        }

        Pair<TriteMenuProvider, TriteMenuObject> menuPair = triteMenuContainer.getTriteMenus().get(clazz);
        TriteMenuProvider key = menuPair.getKey();
        key.onLoad(player, new TriteInventoryContents(menuPair.getValue()));
        triteItemProcessor.initializeItems(menuPair.getValue());

        player.openInventory(menuPair.getValue().getInventory());
        menus.putIfAbsent(player.getUniqueId(), menuPair.getValue());
    }

}
