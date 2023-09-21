package nl.odalitadevelopments.menus;

import lombok.AccessLevel;
import lombok.Getter;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.listeners.InventoryListener;
import nl.odalitadevelopments.menus.listeners.InventoryPacketListener;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.GlobalIdentitySessionCache;
import nl.odalitadevelopments.menus.menu.cache.GlobalPlayerSessionCache;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternContainer;
import nl.odalitadevelopments.menus.providers.ProvidersContainer;
import nl.odalitadevelopments.menus.tasks.MenuTasksProcessor;
import nl.odalitadevelopments.menus.utils.cooldown.CooldownContainer;
import nl.odalitadevelopments.menus.utils.version.ProtocolVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
final class OdalitaMenusImpl implements OdalitaMenus, Listener {

    private static final Map<Plugin, OdalitaMenus> INSTANCES = new HashMap<>();

    static @NotNull OdalitaMenus createInstance(@NotNull JavaPlugin javaPlugin) {
        return new OdalitaMenusImpl(javaPlugin);
    }

    static @NotNull OdalitaMenus getInstance(@NotNull JavaPlugin javaPlugin) {
        OdalitaMenus instance = INSTANCES.get(javaPlugin);
        if (instance == null) {
            throw new IllegalStateException("OdalitaMenus is not initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        return instance;
    }

    static boolean hasInstance(@NotNull JavaPlugin javaPlugin) {
        return INSTANCES.containsKey(javaPlugin);
    }

    private final JavaPlugin javaPlugin;

    private final SupportedMenuTypes supportedMenuTypes;

    private final ItemProcessor itemProcessor;
    private final MenuProcessor menuProcessor;
    private final GlobalPlayerSessionCache globalPlayerSessionCache;
    private final GlobalIdentitySessionCache globalIdentitySessionCache;

    private final PatternContainer patternContainer;

    private final ProvidersContainer providersContainer;
    private final CooldownContainer cooldownContainer;

    @Getter(AccessLevel.NONE)
    private final InventoryListener inventoryListener;
    @Getter(AccessLevel.NONE)
    private final ScheduledFuture<?> menuTask;

    private OdalitaMenusImpl(JavaPlugin javaPlugin) {
        if (INSTANCES.containsKey(javaPlugin)) {
            throw new IllegalStateException("OdalitaMenus is already initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        if (ProtocolVersion.getServerVersion().isEqual(ProtocolVersion.NOT_SUPPORTED)) {
            throw new IllegalStateException("OdalitaMenus does not support this server version! (Versions supported: " + ProtocolVersion.MINECRAFT_1_16_5.format() + " - " + ProtocolVersion.LATEST().format() + ")");
        }

        this.javaPlugin = javaPlugin;

        this.supportedMenuTypes = new SupportedMenuTypes();

        this.itemProcessor = new ItemProcessor();
        this.menuProcessor = new MenuProcessor(this, this.itemProcessor, this.supportedMenuTypes);
        this.globalPlayerSessionCache = new GlobalPlayerSessionCache();
        this.globalIdentitySessionCache = new GlobalIdentitySessionCache(this);

        this.patternContainer = new PatternContainer();

        this.providersContainer = new ProvidersContainer(this);
        this.cooldownContainer = new CooldownContainer();

        this.inventoryListener = new InventoryListener(this, this.menuProcessor);
        new InventoryPacketListener(this, this.menuProcessor);

        javaPlugin.getServer().getPluginManager().registerEvents(this.inventoryListener, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.globalPlayerSessionCache, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.globalIdentitySessionCache, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.cooldownContainer, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);

        this.menuTask = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new MenuTasksProcessor(this), 0, 50, TimeUnit.MILLISECONDS);

        INSTANCES.put(javaPlugin, this);
    }

    @Override
    public @NotNull Collection<@NotNull Player> getPlayersWithOpenMenu() {
        return this.menuProcessor.getPlayersWithOpenMenu();
    }

    @Override
    public @NotNull Collection<@NotNull Player> getPlayersWithOpenMenu(@NotNull String id) {
        return this.menuProcessor.getPlayersWithOpenMenu(id);
    }

    @Override
    public @NotNull Collection<@NotNull MenuSession> getOpenMenuSessions() {
        return this.menuProcessor.getOpenMenuSessions();
    }

    @Override
    public @NotNull Collection<@NotNull MenuSession> getOpenMenuSessions(@NotNull String id) {
        return this.menuProcessor.getOpenMenuSessions(id);
    }

    @Override
    public @Nullable MenuSession getOpenMenuSession(@NotNull Player player) {
        return this.menuProcessor.getOpenMenuSession(player);
    }

    @Override
    public <P extends MenuProvider> void registerProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuProviderLoader<P> loader) {
        this.menuProcessor.registerProviderLoader(providerClass, loader);
    }

    @Override
    public <P extends MenuProvider> boolean isProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.menuProcessor.isProviderLoaderRegistered(providerClass);
    }

    @Override
    public <P extends MenuFrameProvider> void registerFrameProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuFrameProviderLoader<P> loader) {
        this.menuProcessor.getMenuFrameProcessor().registerFrameProviderLoader(providerClass, loader);
    }

    @Override
    public <P extends MenuFrameProvider> boolean isFrameProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.menuProcessor.getMenuFrameProcessor().isFrameProviderLoaderRegistered(providerClass);
    }

    @Override
    public void openMenu(@NotNull MenuProvider menuProvider, @NotNull Player player) {
        this.menuProcessor.openMenu(menuProvider, player);
    }

    @Override
    public <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player) {
        return this.menuProcessor.openMenuBuilder(menuProvider, player);
    }

    @Override
    public <CacheType, T extends MenuPattern<CacheType>> void registerPattern(@NotNull T pattern) {
        this.patternContainer.registerPattern(pattern);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPluginDisable(PluginDisableEvent event) {
        if (this.javaPlugin.equals(event.getPlugin())) {
            this.menuTask.cancel(true);

            for (Player player : this.menuProcessor.getPlayersWithOpenMenu()) {
                player.closeInventory();
            }

            this.providersContainer.close(this);

            HandlerList.unregisterAll(this.inventoryListener);
            HandlerList.unregisterAll(this.globalPlayerSessionCache);
            HandlerList.unregisterAll(this.globalIdentitySessionCache);
            HandlerList.unregisterAll(this.cooldownContainer);
            HandlerList.unregisterAll(this);

            INSTANCES.remove(this.javaPlugin);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand().split(" ")[0].toLowerCase();

        // Listen to commands that stop the server/plugin to close all menus and return placeable items to players if present.
        if (command.endsWith("stop") || command.endsWith("restart") || command.endsWith("reload") || command.endsWith("rl")) {
            for (Player player : this.menuProcessor.getPlayersWithOpenMenu()) {
                player.closeInventory();
            }
        }
    }
}