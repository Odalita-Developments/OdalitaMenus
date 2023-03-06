package nl.odalitadevelopments.menus.tasks;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuTask;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class MenuSchedulerTask implements MenuTaskRunnable {

    @Override
    public void runPerSession(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session) {
        for (MenuTask task : session.getCache().getTasks().values()) {
            if (!task.isStarted() && task.getTicksDelay() <= 0) {
                if (this.runAfterDelay(task, tick)) continue;
            }

            if (task.getUpdatedAtTick() == -1) {
                task.setUpdatedAtTick(tick);
            }

            if (!task.isStarted() && tick - task.getUpdatedAtTick() == task.getTicksDelay()) {
                if (this.runAfterDelay(task, tick)) continue;
            }

            if (!task.isStarted()) continue;

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

    private boolean runAfterDelay(MenuTask task, int tick) {
        task.setUpdatedAtTick(tick);
        task.setStarted(true);

        task.getRunnable().run();

        if (task.getRunTimes() == 1) {
            task.cancel();
            return true;
        }

        return false;
    }
}