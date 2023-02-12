package nl.odalitadevelopments.menus;

import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.menu.session.SessionCache;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternContainer;
import nl.odalitadevelopments.menus.providers.ProvidersContainer;
import nl.odalitadevelopments.menus.utils.cooldown.CooldownContainer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface OdalitaMenus {

    static @NotNull OdalitaMenus createInstance(@NotNull JavaPlugin javaPlugin) {
        return OdalitaMenusImpl.createInstance(javaPlugin);
    }

    static @NotNull OdalitaMenus getInstance(@NotNull JavaPlugin javaPlugin) {
        return OdalitaMenusImpl.getInstance(javaPlugin);
    }

    static boolean hasInstance(@NotNull JavaPlugin javaPlugin) {
        return OdalitaMenusImpl.hasInstance(javaPlugin);
    }

    @NotNull JavaPlugin getJavaPlugin();

    @NotNull SupportedMenuTypes getSupportedMenuTypes();

    @NotNull ItemProcessor getItemProcessor();

    @NotNull MenuProcessor getMenuProcessor();

    @NotNull SessionCache getSessionCache();

    @NotNull PatternContainer getPatternContainer();

    @NotNull ProvidersContainer getProvidersContainer();

    @NotNull CooldownContainer getCooldownContainer();

    <P extends MenuProvider> void registerProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuProviderLoader<P> loader);

    <P extends MenuProvider> boolean isProviderLoaderRegistered(@NotNull Class<P> providerClass);

    <P extends MenuFrameProvider> void registerFrameProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuFrameProviderLoader<P> loader);

    <P extends MenuFrameProvider> boolean isFrameProviderLoaderRegistered(@NotNull Class<P> providerClass);

    <P extends MenuProvider> void openMenu(@NotNull P menuProvider, @NotNull Player player, @NotNull MenuProviderLoader<P> providerLoader);

    void openMenu(@NotNull MenuProvider menuProvider, @NotNull Player player);

    <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player,
                                                                        @NotNull MenuProviderLoader<P> providerLoader);

    <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player);

    <CacheType, T extends MenuPattern<CacheType>> void registerPattern(@NotNull T pattern);
}