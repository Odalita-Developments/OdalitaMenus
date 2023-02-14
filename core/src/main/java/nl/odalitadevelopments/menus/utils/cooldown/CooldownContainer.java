package nl.odalitadevelopments.menus.utils.cooldown;

import com.google.common.base.Preconditions;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class CooldownContainer implements Listener {

    private final Table<UUID, String, Cooldown> playerCooldowns = new Table<>();

    public void addCooldown(UUID uuid, String name, Cooldown cooldown) {
        Preconditions.checkArgument(cooldown.value() > 0, "value must be greater than 0");

        this.playerCooldowns.computeIfAbsent(uuid, name, (u, s) -> cooldown);
    }

    public void addCooldown(UUID uuid, String name, int value, TimeUnit timeUnit) {
        this.addCooldown(uuid, name, new Cooldown(value, timeUnit));
    }

    public boolean hasCooldown(UUID uuid, String name) {
        Cooldown cooldown = this.playerCooldowns.get(uuid, name);
        if (cooldown == null) return false;

        if (cooldown.isExpired()) {
            this.playerCooldowns.remove(uuid, name);
            return false;
        }

        return true;
    }

    public void removeCooldown(UUID uuid, String name) {
        this.playerCooldowns.remove(uuid, name);
    }

    public boolean checkAndCreate(UUID uuid, String name, Cooldown cooldown) {
        if (this.hasCooldown(uuid, name)) return true;

        this.addCooldown(uuid, name, cooldown);
        return false;
    }

    public boolean checkAndCreate(UUID uuid, String name, int value, TimeUnit timeUnit) {
        return this.checkAndCreate(uuid, name, Cooldown.of(value, timeUnit));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.playerCooldowns.getRowMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        this.playerCooldowns.getRowMap().remove(event.getPlayer().getUniqueId());
    }
}