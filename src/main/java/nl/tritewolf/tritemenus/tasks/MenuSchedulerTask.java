package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class MenuSchedulerTask implements MenuTaskRunnable {

    @Override
    public void run(@NotNull TriteMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session) {
        for (MenuTask task : session.getCache().getTasks().values()) {
            if (!task.isStarted() && task.getTicksDelay() <= 0) {
                if (this.runAfterDelay(task, tick)) continue;
            }

            if (task.getUpdatedAtTick() == -1) {
                task.setUpdatedAtTick(tick);
            }

            if (!task.isStarted() && tick - task.getUpdatedAtTick() >= task.getTicksDelay()) {
                if (this.runAfterDelay(task, tick)) continue;
            }

            if (tick - task.getUpdatedAtTick() == task.getTicksPeriod()) {
                task.setUpdatedAtTick(tick);

                task.getRunnable().run();
                task.setRanTimes(task.getRanTimes() + 1);
            }

            if (task.getRunTimes() > 0 && task.getRanTimes() >= task.getRunTimes()) {
                task.cancel();
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