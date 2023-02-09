package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class MenuTasksProcessor implements Runnable {

    private final AtomicInteger tickCounter = new AtomicInteger(0);

    private final Collection<MenuTaskRunnable> tasks = new HashSet<>();

    private final TriteMenus instance;
    private final MenuProcessor menuProcessor;

    public MenuTasksProcessor(TriteMenus instance) {
        this.instance = instance;
        this.menuProcessor = instance.getMenuProcessor();

        this.tasks.add(new MenuSchedulerTask());
        this.tasks.add(new MenuUpdateTask());
    }

    @Override
    public void run() {
        int tick = this.tickCounter.getAndIncrement();

        Map<Player, MenuSession> openMenus = this.menuProcessor.getOpenMenus();
        if (openMenus.isEmpty()) return;

        for (Map.Entry<Player, MenuSession> entry : openMenus.entrySet()) {
            MenuSession menuSession = entry.getValue();
            if (menuSession == null) continue;

            Player player = entry.getKey();
            if (player == null || !player.isOnline()) continue;

            for (MenuTaskRunnable runnable : this.tasks) {
                runnable.run(this.instance, this.menuProcessor, tick, player, menuSession);
            }
        }
    }
}