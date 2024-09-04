package nl.odalitadevelopments.menus.utils.cooldown;

import com.google.common.base.Preconditions;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.MenuData;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class CooldownContainer implements Listener {

    private final Table<UUID, String, Cooldown> sessionCooldowns = new Table<>();

    private final OdalitaMenus instance;

    public CooldownContainer(OdalitaMenus instance) {
        this.instance = instance;
    }

    public void addCooldown(AbstractMenuSession<?, ?, ?> menuSession, String name, Cooldown cooldown) {
        Preconditions.checkArgument(cooldown.value() > 0, "value must be greater than 0");

        this.sessionCooldowns.computeIfAbsent(this.getUniqueId(menuSession), name, (u, s) -> cooldown);
    }

    public boolean hasCooldown(AbstractMenuSession<?, ?, ?> menuSession, String name) {
        UUID uniqueId = this.getUniqueId(menuSession);

        Cooldown cooldown = this.sessionCooldowns.get(uniqueId, name);
        if (cooldown == null) return false;

        if (cooldown.isExpired()) {
            this.sessionCooldowns.remove(uniqueId, name);
            return false;
        }

        return true;
    }

    public boolean checkAndCreate(AbstractMenuSession<?, ?, ?> menuSession, String name, Cooldown cooldown) {
        if (this.hasCooldown(menuSession, name)) return true;

        this.addCooldown(menuSession, name, cooldown);
        return false;
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.cleanup(event.getPlayer());
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        this.cleanup(event.getPlayer());
    }

    private void cleanup(Player player) {
        MenuSession menuSession = this.instance.getOpenMenuSession(player);
        if (menuSession == null || !menuSession.getViewers().isEmpty()) return;

        this.sessionCooldowns.getRowMap().remove(menuSession.getUniqueId());
    }

    private UUID getUniqueId(AbstractMenuSession<?, ?, ?> menuSession) {
        return menuSession.data().getOrThrow(MenuData.Key.UNIQUE_ID);
    }
}