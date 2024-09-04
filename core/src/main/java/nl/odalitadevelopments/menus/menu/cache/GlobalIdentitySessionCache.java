package nl.odalitadevelopments.menus.menu.cache;

import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuData;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO merge global session cache into one class by using menu unique id instead of player uuid
@RequiredArgsConstructor
public final class GlobalIdentitySessionCache implements Listener {

    private final OdalitaMenus instance;

    private final Table<UUID, String, Map<String, Object>> identityMenuCache = new Table<>();

    public Map<String, Object> getOrCreateCache(@NotNull MenuData data) {
        UUID uuid = data.getOrThrow(MenuData.Key.UNIQUE_ID);
        String globalCacheKey = data.getOrThrow(MenuData.Key.GLOBAL_CACHE_KEY);

        Map<String, Object> cache = this.identityMenuCache.get(uuid, globalCacheKey);
        if (cache == null) {
            cache = new HashMap<>();
            this.identityMenuCache.put(uuid, globalCacheKey, cache);
        }

        return cache;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            this.cleanPlayerCache(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.cleanPlayerCache(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerKick(PlayerKickEvent event) {
        this.cleanPlayerCache(event.getPlayer());
    }

    private void cleanPlayerCache(Player player) {
        MenuSession menuSession = this.instance.getOpenMenuSession(player);
        if (menuSession == null || !menuSession.getViewers().isEmpty()) return;

        this.identityMenuCache.getRowMap().remove(menuSession.data().getOrThrow(MenuData.Key.UNIQUE_ID));
    }
}