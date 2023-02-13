package nl.odalitadevelopments.menus;

import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternContainer;
import nl.odalitadevelopments.menus.providers.ProvidersContainer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class OdalitaMenusPlugin extends JavaPlugin {

    public static @NotNull OdalitaMenusPlugin getInstance() {
        return JavaPlugin.getPlugin(OdalitaMenusPlugin.class);
    }

    private OdalitaMenus odalitaMenus;

    @Override
    public void onEnable() {
        this.odalitaMenus = OdalitaMenus.createInstance(this);
    }

    public @NotNull OdalitaMenus getOdalitaMenus() {
        return this.odalitaMenus;
    }

    public @NotNull MenuProcessor getMenuProcessor() {
        return this.odalitaMenus.getMenuProcessor();
    }

    public @NotNull PatternContainer getPatternContainer() {
        return this.odalitaMenus.getPatternContainer();
    }

    public @NotNull ProvidersContainer getProvidersContainer() {
        return this.odalitaMenus.getProvidersContainer();
    }

    public <P extends MenuProvider> void registerProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuProviderLoader<P> loader) {
        this.odalitaMenus.registerProviderLoader(providerClass, loader);
    }

    public <P extends MenuProvider> boolean isProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.odalitaMenus.isProviderLoaderRegistered(providerClass);
    }

    public <P extends MenuFrameProvider> void registerFrameProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuFrameProviderLoader<P> loader) {
        this.odalitaMenus.registerFrameProviderLoader(providerClass, loader);
    }

    public <P extends MenuFrameProvider> boolean isFrameProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.odalitaMenus.isFrameProviderLoaderRegistered(providerClass);
    }

    public void openMenu(@NotNull MenuProvider menuProvider, @NotNull Player player) {
        this.odalitaMenus.openMenu(menuProvider, player);
    }

    public <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player) {
        return this.odalitaMenus.openMenuBuilder(menuProvider, player);
    }

    public <CacheType, T extends MenuPattern<CacheType>> void registerPattern(@NotNull T pattern) {
        this.odalitaMenus.registerPattern(pattern);
    }
}