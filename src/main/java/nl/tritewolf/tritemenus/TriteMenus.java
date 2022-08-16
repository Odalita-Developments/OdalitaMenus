package nl.tritewolf.tritemenus;

import lombok.Getter;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.listeners.InventoryListener;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.type.SupportedMenuTypes;
import nl.tritewolf.tritemenus.patterns.PatternContainer;
import nl.tritewolf.tritemenus.providers.ProvidersContainer;
import nl.tritewolf.tritemenus.tasks.MenuSchedulerTask;
import nl.tritewolf.tritemenus.tasks.MenuUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public final class TriteMenus {

    private static TriteMenus INSTANCE;

    private final JavaPlugin javaPlugin;

    private final SupportedMenuTypes supportedMenuTypes;

    private final ItemProcessor itemProcessor;
    private final MenuProcessor menuProcessor;

    private final PatternContainer patternContainer;

    private final ProvidersContainer providersContainer;

    public TriteMenus(JavaPlugin javaPlugin) {
        INSTANCE = this;

        this.javaPlugin = javaPlugin;

        this.supportedMenuTypes = new SupportedMenuTypes();

        this.itemProcessor = new ItemProcessor();
        this.menuProcessor = new MenuProcessor(this.itemProcessor, this.supportedMenuTypes);

        this.patternContainer = new PatternContainer();

        this.providersContainer = new ProvidersContainer();

        javaPlugin.getServer().getPluginManager().registerEvents(new InventoryListener(this.menuProcessor), javaPlugin);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new MenuUpdateTask(this.menuProcessor), 0, 50, TimeUnit.MILLISECONDS);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new MenuSchedulerTask(this.menuProcessor), 0, 50, TimeUnit.MILLISECONDS);
    }

    @ApiStatus.Internal
    public static TriteMenus getInstance() {
        return INSTANCE;
    }
}