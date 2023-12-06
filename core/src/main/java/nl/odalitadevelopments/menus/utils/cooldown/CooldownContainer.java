package nl.odalitadevelopments.menus.utils.cooldown;

import com.google.common.base.Preconditions;
import nl.odalitadevelopments.menus.OdalitaMenus;
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

    public void addCooldown(MenuSession menuSession, String name, Cooldown cooldown) {
        Preconditions.checkArgument(cooldown.value() > 0, "value must be greater than 0");

        this.sessionCooldowns.computeIfAbsent(menuSession.getUniqueId(), name, (u, s) -> cooldown);
    }

    public boolean hasCooldown(MenuSession menuSession, String name) {
        Cooldown cooldown = this.sessionCooldowns.get(menuSession.getUniqueId(), name);
        if (cooldown == null) return false;

        if (cooldown.isExpired()) {
            this.sessionCooldowns.remove(menuSession.getUniqueId(), name);
            return false;
        }

        return true;
    }

    public boolean checkAndCreate(MenuSession menuSession, String name, Cooldown cooldown) {
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
}