package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class MenuSchedulerTask implements Runnable {

    @TriteJect
    private MenuProcessor menuProcessor;

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
                if (task.getTicksDelay() == 0) {
                    task.setStarted(true);
                }

                if (!task.isStarted() && task.getStartedAtTick() == -1 && task.getTicksDelay() > 0) {
                    task.setStartedAtTick(ticks);
                    continue;
                }

                if (!task.isStarted() && task.getStartedAtTick() + ticks >= task.getTicksDelay()) {
                    task.getRunnable().run();
                    task.setStarted(true);

                    if (task.getRunTimes() == 1) {
                        task.cancel();
                        continue;
                    }
                }

                if (ticks % task.getTicksPeriod() == 0) {
                    task.getRunnable().run();
                    task.setRanTimes(task.getRanTimes() + 1);
                }

                if (task.getRanTimes() >= task.getRunTimes()) {
                    task.cancel();
                }
            }
        }
    }
}