package nl.odalitadevelopments.menus.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public interface OdalitaEventListener extends Listener, EventExecutor {

    default void unregister() {
        HandlerList.unregisterAll(this);
    }
}