package nl.odalitadevelopments.menus.contents;

import org.jetbrains.annotations.NotNull;

public interface InventoryContentsScheduler {

    @NotNull
    MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod, int runTimes);

    @NotNull
    MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod);

    @NotNull
    MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod, int runTimes);

    @NotNull
    MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod);

    boolean isRunning(@NotNull String id);

    void cancel(@NotNull String id);
}