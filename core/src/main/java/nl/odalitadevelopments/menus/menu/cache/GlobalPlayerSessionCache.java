package nl.odalitadevelopments.menus.menu.cache;

import nl.odalitadevelopments.menus.menu.MenuData;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GlobalPlayerSessionCache implements Listener {

    private final Table<UUID, String, Map<String, Object>> playerMenuCache = new Table<>();

    public Map<String, Object> getOrCreateCache(@NotNull MenuData data) {
        UUID uuid = menuSession.getViewer().getUniqueId();
        String globalCacheKey = menuSession.getGlobalCacheKey();

        Map<String, Object> cache = this.playerMenuCache.get(uuid, globalCacheKey);
        if (cache == null) {
            cache = new HashMap<>();
            this.playerMenuCache.put(uuid, globalCacheKey, cache);
        }

        return cache;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.cleanPlayerCache(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerKick(PlayerKickEvent event) {
        this.cleanPlayerCache(event.getPlayer().getUniqueId());
    }

    private void cleanPlayerCache(UUID playerUuid) {
        this.playerMenuCache.getRowMap().remove(playerUuid);
    }
}