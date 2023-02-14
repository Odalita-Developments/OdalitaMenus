package nl.odalitadevelopments.menus.tasks;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuTaskRunnable {

    default void runGlobally(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick) {
    }

    void runPerSession(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session);
}