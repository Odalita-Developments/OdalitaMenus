package nl.tritewolf.tritemenus.contents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MenuTask {

    private final InventoryContentsScheduler scheduler;

    private final String id;
    private final Runnable runnable;

    private final int ticksDelay;
    private final int ticksPeriod;
    private final int runTimes;

    private boolean started = false;
    private int updatedAtTick = -1;
    private int ranTimes = 0;

    MenuTask(InventoryContentsScheduler scheduler, String id, Runnable runnable, int ticksDelay, int ticksPeriod, int runTimes) {
        this.scheduler = scheduler;

        this.id = id;
        this.runnable = runnable;
        this.ticksDelay = ticksDelay;
        this.ticksPeriod = ticksPeriod;
        this.runTimes = runTimes;
    }

    public synchronized void cancel() {
        this.scheduler.cancel(this.id);
    }
}