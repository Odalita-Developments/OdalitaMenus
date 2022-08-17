package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public record MenuSchedulerTask(MenuProcessor menuProcessor) implements Runnable {

    private static final AtomicInteger TICKS = new AtomicInteger(0);

    @Override
    public void run() {
        Map<Player, MenuSession> openMenus = this.menuProcessor.getOpenMenus();
        if (openMenus.isEmpty()) {
            TICKS.set(0);
            return;
        }

        int ticks = TICKS.incrementAndGet();

        for (Map.Entry<Player, MenuSession> entry : openMenus.entrySet()) {
            MenuSession menuSession = entry.getValue();
            if (menuSession == null || menuSession.getCache().getTasks().isEmpty()) continue;

            Player player = entry.getKey();
            if (player == null || !player.isOnline()) continue;

            for (MenuTask task : menuSession.getCache().getTasks().values()) {
                if (!task.isStarted() && task.getTicksDelay() <= 0) {
                    if (this.runAfterDelay(task, ticks)) continue;
                }

                if (task.getUpdatedAtTick() == -1) {
                    task.setUpdatedAtTick(ticks);
                }

                if (!task.isStarted() && ticks - task.getUpdatedAtTick() >= task.getTicksDelay()) {
                    if (this.runAfterDelay(task, ticks)) continue;
                }

                if (ticks - task.getUpdatedAtTick() == task.getTicksPeriod()) {
                    task.setUpdatedAtTick(ticks);

                    task.getRunnable().run();
                    task.setRanTimes(task.getRanTimes() + 1);
                }

                if (task.getRunTimes() > 0 && task.getRanTimes() >= task.getRunTimes()) {
                    task.cancel();
                }
            }
        }
    }

    private boolean runAfterDelay(MenuTask task, int ticks) {
        task.setUpdatedAtTick(ticks);
        task.setStarted(true);

        task.getRunnable().run();

        if (task.getRunTimes() == 1) {
            task.cancel();
            return true;
        }

        return false;
    }
}