package nl.odalitadevelopments.menus.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BukkitThreadHelper {

    private BukkitThreadHelper() {
    }

    public static void runSync(@NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runAsync(@NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    public static void runCondition(boolean async, @NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        if (async) {
            runAsync(plugin, runnable);
        } else {
            runSync(plugin, runnable);
        }
    }
}