package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.listeners.InventoryListener;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.session.SessionCache;
import nl.tritewolf.tritemenus.menu.type.SupportedMenuTypes;
import nl.tritewolf.tritemenus.patterns.PatternContainer;
import nl.tritewolf.tritemenus.providers.ProvidersContainer;
import nl.tritewolf.tritemenus.tasks.MenuTasksProcessor;
import nl.tritewolf.tritemenus.utils.cooldown.CooldownContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
public final class TriteMenus implements Listener {

    private static final Map<Plugin, TriteMenus> INSTANCES = new HashMap<>();

    public static @NotNull TriteMenus createInstance(@NotNull JavaPlugin javaPlugin) {
        return new TriteMenus(javaPlugin);
    }

    public static @NotNull TriteMenus getInstance(@NotNull JavaPlugin javaPlugin) {
        TriteMenus instance = INSTANCES.get(javaPlugin);
        if (instance == null) {
            throw new IllegalStateException("TriteMenus is not initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        return instance;
    }

    public static boolean hasInstance(@NotNull JavaPlugin javaPlugin) {
        return INSTANCES.containsKey(javaPlugin);
    }

    private final JavaPlugin javaPlugin;

    private final SupportedMenuTypes supportedMenuTypes;

    private final ItemProcessor itemProcessor;
    private final MenuProcessor menuProcessor;
    private final SessionCache sessionCache;

    private final PatternContainer patternContainer;

    private final ProvidersContainer providersContainer;
    private final CooldownContainer cooldownContainer;

    private final InventoryListener inventoryListener;

    private final ScheduledFuture<?> menuTask;

    private TriteMenus(JavaPlugin javaPlugin) {
        if (INSTANCES.containsKey(javaPlugin)) {
            throw new IllegalStateException("TriteMenus is already initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        this.javaPlugin = javaPlugin;

        this.supportedMenuTypes = new SupportedMenuTypes();

        this.itemProcessor = new ItemProcessor();
        this.menuProcessor = new MenuProcessor(this, this.itemProcessor, this.supportedMenuTypes);
        this.sessionCache = new SessionCache();

        this.patternContainer = new PatternContainer();

        this.providersContainer = new ProvidersContainer();
        this.cooldownContainer = new CooldownContainer();

        this.inventoryListener = new InventoryListener(this, this.menuProcessor);
        javaPlugin.getServer().getPluginManager().registerEvents(this.inventoryListener, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.sessionCache, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);

        this.menuTask = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new MenuTasksProcessor(this), 0, 50, TimeUnit.MILLISECONDS);

        INSTANCES.put(javaPlugin, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPluginDisable(PluginDisableEvent event) {
        if (this.javaPlugin.equals(event.getPlugin())) {
            for (Player player : this.menuProcessor.getOpenMenus().keySet()) {
                player.closeInventory();
            }

            this.menuTask.cancel(true);

            HandlerList.unregisterAll(this.inventoryListener);
            HandlerList.unregisterAll(this.sessionCache);
            HandlerList.unregisterAll(this);

            INSTANCES.remove(this.javaPlugin);
        }
    }
}