package nl.odalitadevelopments.menus.menu.cache;

import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class GlobalIdentitySessionCache implements Listener {

    private final OdalitaMenus instance;

    private final Table<UUID, String, Map<String, Object>> identityMenuCache = new Table<>();

    public Map<String, Object> getOrCreateCache(@NotNull MenuSession menuSession) {
        UUID uuid = menuSession.getUniqueId();
        String globalCacheKey = menuSession.getGlobalCacheKey();

        Map<String, Object> cache = this.identityMenuCache.get(uuid, globalCacheKey);
        if (cache == null) {
            cache = new HashMap<>();
            this.identityMenuCache.put(uuid, globalCacheKey, cache);
        }

        return cache;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        MenuSession menuSession = this.instance.getOpenMenuSession(player);
        if (menuSession == null || !menuSession.getViewers().isEmpty()) return;

        this.identityMenuCache.getRowMap().remove(menuSession.getUniqueId());
    }
}