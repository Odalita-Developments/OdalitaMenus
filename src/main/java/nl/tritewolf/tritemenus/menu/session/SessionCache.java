package nl.tritewolf.tritemenus.menu.session;

import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.utils.collection.Table;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class SessionCache implements Listener {

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
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.playerMenuCache.removeIf((uuid, key, map) -> uuid.equals(event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        this.playerMenuCache.removeIf((uuid, key, map) -> uuid.equals(event.getPlayer().getUniqueId()));
    }
}