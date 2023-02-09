package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MenuTaskRunnable {

    void run(@NotNull TriteMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session);
}