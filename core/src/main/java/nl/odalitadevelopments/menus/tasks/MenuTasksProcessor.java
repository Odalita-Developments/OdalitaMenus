package nl.odalitadevelopments.menus.tasks;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class MenuTasksProcessor implements Runnable {

    private static final AtomicInteger TICK_COUNTER = new AtomicInteger(0);

    private final Collection<MenuTaskRunnable> tasks = new HashSet<>();

    private final OdalitaMenus instance;
    private final MenuProcessor menuProcessor;

    public MenuTasksProcessor(OdalitaMenus instance) {
        this.instance = instance;
        this.menuProcessor = instance.getMenuProcessor();

        this.tasks.add(new MenuSchedulerTask());
        this.tasks.add(new MenuUpdateTask());
    }

    @Override
    public void run() {
        try {
            int tick = TICK_COUNTER.getAndIncrement();

            for (MenuTaskRunnable runnable : this.tasks) {
                runnable.runGlobally(this.instance, this.menuProcessor, tick);
            }

            Map<Player, MenuSession> openMenus = this.menuProcessor.getOpenMenus();

            for (MenuSession menuSession : openMenus.values()) {
                if (menuSession == null || menuSession.isClosed()) continue;

                for (MenuTaskRunnable runnable : this.tasks) {
                    runnable.runPerSession(this.instance, this.menuProcessor, tick, menuSession);
                }
            }
        } catch (Exception exception) {
            OdalitaLogger.error(exception);
        }
    }
}