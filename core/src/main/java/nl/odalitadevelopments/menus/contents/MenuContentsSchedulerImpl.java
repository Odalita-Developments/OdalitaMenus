package nl.odalitadevelopments.menus.contents;

import org.jetbrains.annotations.NotNull;

record MenuContentsSchedulerImpl(MenuContentsImpl inventoryContents) implements MenuContentsScheduler {

    @Override
    public @NotNull MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod, int runTimes) {
        if (ticksDelay < 0 || ticksPeriod < 0) {
            throw new IllegalArgumentException("Delay, period must be positive");
        }

        synchronized (this.inventoryContents.cache.getTasks()) {
            runTimes = Math.max(runTimes, 0);

            if (this.inventoryContents.cache.getTasks().containsKey(id)) {
                throw new IllegalArgumentException("Task with id '" + id + "' already exists");
            }

            MenuTask task = new MenuTask(this, id, runnable, ticksDelay, ticksPeriod, runTimes);
            this.inventoryContents.cache.getTasks().put(task.getId(), task);
            return task;
        }
    }

    @Override
    public @NotNull MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay, int ticksPeriod) {
        return this.delay(id, runnable, ticksDelay, ticksPeriod, 0);
    }

    @Override
    public @NotNull MenuTask delay(@NotNull String id, @NotNull Runnable runnable, int ticksDelay) {
        return this.delay(id, runnable, ticksDelay, 0, 1);
    }

    @Override
    public @NotNull MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod, int runTimes) {
        return this.delay(id, runnable, 0, ticksPeriod, runTimes);
    }

    @Override
    public @NotNull MenuTask schedule(@NotNull String id, @NotNull Runnable runnable, int ticksPeriod) {
        return this.schedule(id, runnable, ticksPeriod, 0);
    }

    @Override
    public boolean isRunning(@NotNull String id) {
        synchronized (this.inventoryContents.cache.getTasks()) {
            return this.inventoryContents.cache.getTasks().containsKey(id);
        }
    }

    @Override
    public void cancel(@NotNull String id) {
        synchronized (this.inventoryContents.cache.getTasks()) {
            this.inventoryContents.cache.getTasks().remove(id);
        }
    }
}