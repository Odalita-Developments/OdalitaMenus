package nl.odalitadevelopments.menus;

import lombok.AccessLevel;
import lombok.Getter;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.listeners.InventoryListener;
import nl.odalitadevelopments.menus.listeners.InventoryPacketListener;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.GlobalSessionCache;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import nl.odalitadevelopments.menus.nms.utils.version.ProtocolVersion;
import nl.odalitadevelopments.menus.nms.v1_16_R3.OdalitaMenusNMS_v1_16_R5;
import nl.odalitadevelopments.menus.nms.v1_17_R1.OdalitaMenusNMS_v1_17_R1;
import nl.odalitadevelopments.menus.nms.v1_18_R2.OdalitaMenusNMS_v1_18_R2;
import nl.odalitadevelopments.menus.nms.v1_19_R3.OdalitaMenusNMS_v1_19_R3;
import nl.odalitadevelopments.menus.nms.v1_20_R1.OdalitaMenusNMS_v1_20_R1;
import nl.odalitadevelopments.menus.nms.v1_20_R3.OdalitaMenusNMS_v1_20_R3;
import nl.odalitadevelopments.menus.nms.v1_20_R4.OdalitaMenusNMS_v1_20_R4;
import nl.odalitadevelopments.menus.nms.v1_21_R1.OdalitaMenusNMS_v1_21_R1;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternContainer;
import nl.odalitadevelopments.menus.providers.ProvidersContainer;
import nl.odalitadevelopments.menus.tasks.MenuTasksProcessor;
import nl.odalitadevelopments.menus.utils.OdalitaServicesHandler;
import nl.odalitadevelopments.menus.utils.cooldown.CooldownContainer;
import nl.odalitadevelopments.menus.utils.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;

@Getter
final class OdalitaMenusImpl implements OdalitaMenus, Listener {

    static @NotNull OdalitaMenus createInstance(@NotNull JavaPlugin javaPlugin) {
        return new OdalitaMenusImpl(javaPlugin);
    }

    private static final String VERSION = "0.5.11";

    private final JavaPlugin javaPlugin;
    private final int id;

    private final SupportedMenuTypes supportedMenuTypes;

    private final ItemProcessor itemProcessor;
    private final MenuProcessor menuProcessor;
    private final GlobalSessionCache globalSessionCache;

    private final PatternContainer patternContainer;

    private final ProvidersContainer providersContainer;
    private final CooldownContainer cooldownContainer;

    @Getter(AccessLevel.NONE)
    private final InventoryListener inventoryListener;
    @Getter(AccessLevel.NONE)
    private final BukkitTask menuTask;

    private OdalitaMenusImpl(JavaPlugin javaPlugin) {
        OdalitaServicesHandler servicesHandler = new OdalitaServicesHandler(javaPlugin);
        this.id = servicesHandler.generateId();

        this.initNMS();

        this.javaPlugin = javaPlugin;

        this.supportedMenuTypes = new SupportedMenuTypes();

        this.itemProcessor = new ItemProcessor();
        this.menuProcessor = new MenuProcessor(this, this.itemProcessor, this.supportedMenuTypes);
        this.globalSessionCache = new GlobalSessionCache();

        this.patternContainer = new PatternContainer();

        this.providersContainer = new ProvidersContainer(this);
        this.cooldownContainer = new CooldownContainer();

        this.inventoryListener = new InventoryListener(this, this.menuProcessor);
        new InventoryPacketListener(this, this.menuProcessor);

        javaPlugin.getServer().getPluginManager().registerEvents(this.inventoryListener, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.globalSessionCache, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this.cooldownContainer, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);

        this.menuTask = Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, new MenuTasksProcessor(this), 0, 1);

        Bukkit.getServicesManager().register(int.class, 0, javaPlugin, ServicePriority.Normal);

        new Metrics(javaPlugin, 22559, VERSION);
    }

    private void initNMS() {
        try {
            Class<?> harmNMSInstance = Class.forName("nl.odalitadevelopments.menus.nms.OdalitaMenusNMSInstance");
            OdalitaMenusNMS nms = switch (ProtocolVersion.getServerVersion()) {
                case MINECRAFT_1_21 -> new OdalitaMenusNMS_v1_21_R1();
                case MINECRAFT_1_20_6 -> new OdalitaMenusNMS_v1_20_R4();
                case MINECRAFT_1_20_4 -> new OdalitaMenusNMS_v1_20_R3();
                case MINECRAFT_1_20_1 -> new OdalitaMenusNMS_v1_20_R1();
                case MINECRAFT_1_19_4 -> new OdalitaMenusNMS_v1_19_R3();
                case MINECRAFT_1_18_2 -> new OdalitaMenusNMS_v1_18_R2();
                case MINECRAFT_1_17_1 -> new OdalitaMenusNMS_v1_17_R1();
                case MINECRAFT_1_16_5 -> new OdalitaMenusNMS_v1_16_R5();
                default ->
                        throw new IllegalStateException("OdalitaMenus does not support this server version! (Versions supported: " + ProtocolVersion.MINECRAFT_1_16_5.format() + " - " + ProtocolVersion.latest().format() + ")");
            };

            Method method = harmNMSInstance.getDeclaredMethod("init", OdalitaMenusNMS.class);
            method.setAccessible(true);
            method.invoke(null, nms);
            method.setAccessible(false);
        } catch (Exception exception) {
            OdalitaLogger.error(exception);
            Bukkit.getServer().shutdown();
        }
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
            this.menuTask.cancel();

            for (Player player : this.menuProcessor.getPlayersWithOpenMenu()) {
                player.closeInventory();
            }

            HandlerList.unregisterAll(this.inventoryListener);
            HandlerList.unregisterAll(this.globalSessionCache);
            HandlerList.unregisterAll(this.cooldownContainer);
            HandlerList.unregisterAll(this);

            this.javaPlugin.getServer().getServicesManager().unregisterAll(this.javaPlugin);
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