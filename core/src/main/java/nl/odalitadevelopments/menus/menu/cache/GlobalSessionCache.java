package nl.odalitadevelopments.menus.menu.cache;

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

public final class GlobalSessionCache implements Listener {

    private final Table<UUID, String, Map<String, Object>> playerMenuCache = new Table<>();

    public Map<String, Object> getOrCreateCache(@NotNull MenuSession menuSession) {
        UUID uuid = menuSession.getPlayer().getUniqueId();
        String globalCacheKey = menuSession.getGlobalCacheKey();

        Map<String, Object> cache = this.playerMenuCache.get(uuid, globalCacheKey);
        if (cache == null) {
            cache = new HashMap<>();
            this.playerMenuCache.put(uuid, globalCacheKey, cache);
        }

        return cache;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.playerMenuCache.removeIf((uuid, key, map) -> uuid.equals(event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerKick(PlayerKickEvent event) {
        this.playerMenuCache.removeIf((uuid, key, map) -> uuid.equals(event.getPlayer().getUniqueId()));
    }
}