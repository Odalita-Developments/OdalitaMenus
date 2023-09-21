package nl.odalitadevelopments.menus.contents.scheduler;

import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuContentsScheduler permits MenuContentsSchedulerImpl {

    @Contract("_ -> new")
    @ApiStatus.Internal
    static @NotNull MenuContentsScheduler create(@NotNull MenuSessionCache cache) {
        return new MenuContentsSchedulerImpl(cache);
    }

    @NotNull
    MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod, int runTimes);

    @NotNull
    MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod);

    @NotNull
    MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay);

    @NotNull
    MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod, int runTimes);

    @NotNull
    MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod);

    boolean isRunning(@NotNull String id);

    void cancel(@NotNull String id);
}