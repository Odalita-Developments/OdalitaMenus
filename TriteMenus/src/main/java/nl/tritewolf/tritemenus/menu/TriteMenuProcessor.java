package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.val;
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
    private final Map<UUID, Map<Class<?>, Pair<TriteMenuProvider, TriteMenuObject>>> menus = new HashMap<>();
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
                menuObject.setHasMenuOpened(true);
                return;
            }

            Pair<TriteGlobalMenuProvider, TriteMenuObject> globalMenuPair = triteMenuContainer.getTriteGlobalMenus().get(clazz);
            TriteGlobalMenuProvider key = globalMenuPair.getKey();
            key.onLoad(new TriteInventoryContents(globalMenuPair.getValue()));
            triteItemProcessor.initializeItems(globalMenuPair.getValue());

            player.openInventory(globalMenuPair.getValue().getInventory());
            globalMenuPair.getValue().setHasMenuOpened(true);

            globalMenus.putIfAbsent(clazz, globalMenuPair.getValue());
            return;
        }

        Map<Class<?>, Pair<TriteMenuProvider, TriteMenuObject>> menuObject = menus.get(player.getUniqueId());
        if (menuObject != null) {
            Pair<TriteMenuProvider, TriteMenuObject> triteMenuObjectPair = menuObject.get(clazz);
            player.openInventory(triteMenuObjectPair.getValue().getInventory());
            triteMenuObjectPair.getValue().setHasMenuOpened(true);
            return;
        }

        try {
            TriteMenuProvider menuProviderBase = triteMenuContainer.getTriteMenus().get(clazz);
            TriteMenuProvider triteMenuProvider = menuProviderBase.getClass().getDeclaredConstructor().newInstance();
            val annotation = triteMenuProvider.getClass().getAnnotation(TriteMenu.class);

            if (annotation.menuType().equals(TriteMenuType.GLOBAL)) {
                //todo THROW EXEPTION
                return;
            }

            val triteMenuObject = new TriteMenuObject(annotation.rows(), annotation.displayName(), annotation.menuType());

            triteMenuProvider.onLoad(player, new TriteInventoryContents(triteMenuObject));
            triteItemProcessor.initializeItems(triteMenuObject);

            player.openInventory(triteMenuObject.getInventory());
            triteMenuObject.setHasMenuOpened(true);
            menus.computeIfAbsent(player.getUniqueId(), uuid -> new HashMap<>()).put(clazz, new Pair<>(triteMenuProvider, triteMenuObject));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
