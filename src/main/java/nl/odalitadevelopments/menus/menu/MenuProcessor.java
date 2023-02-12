package nl.odalitadevelopments.menus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter(AccessLevel.PACKAGE)
public final class MenuProcessor {

    private final OdalitaMenus instance;
    private final ItemProcessor itemProcessor;
    private final SupportedMenuTypes supportedMenuTypes;

    @Getter
    private final MenuFrameProcessor menuFrameProcessor;

    @Getter
    private final Map<Class<? extends MenuProvider>, MenuProviderLoader<?>> providerLoaders = new ConcurrentHashMap<>();

    private final Map<Class<? extends MenuProvider>, MenuProviderLoader<?>> providerLoaderCache = new HashMap<>();

    @Getter
    private final Map<Player, MenuSession> openMenus = new ConcurrentHashMap<>();

    public MenuProcessor(OdalitaMenus instance, ItemProcessor itemProcessor, SupportedMenuTypes supportedMenuTypes) {
        this.instance = instance;
        this.itemProcessor = itemProcessor;
        this.supportedMenuTypes = supportedMenuTypes;

        this.menuFrameProcessor = new MenuFrameProcessor(this);
    }

    public <P extends MenuProvider> void registerProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuProviderLoader<P> loader) {
        MenuProviderLoader<?> previous = this.providerLoaders.putIfAbsent(providerClass, loader);
        if (previous != null) {
            throw new IllegalStateException("Provider loader for '" + providerClass.getName() + "' already registered");
        }
    }

    public <P extends MenuProvider> boolean isProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.providerLoaders.containsKey(providerClass);
    }

    public <P extends MenuProvider> void openMenu(@NotNull P menuProvider, @NotNull Player player, @NotNull MenuProviderLoader<P> providerLoader) {
        this.openMenuBuilder(menuProvider, player, providerLoader)
                .open();
    }

    public void openMenu(@NotNull MenuProvider menuProvider, @NotNull Player player) {
        this.openMenuBuilder(menuProvider, player)
                .open();
    }

    public <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player,
                                                                               @NotNull MenuProviderLoader<P> providerLoader) {
        return new MenuOpenerBuilderImpl<>(this, this.itemProcessor, this.supportedMenuTypes, menuProvider, player, providerLoader);
    }

    @SuppressWarnings("unchecked")
    public <P extends MenuProvider> @NotNull MenuOpenerBuilder openMenuBuilder(@NotNull P menuProvider, @NotNull Player player) {
        MenuProviderLoader<P> providerLoader = (MenuProviderLoader<P>) this.providerLoaderCache.get(menuProvider.getClass());
        if (providerLoader == null) {
            Class<?> providerLoaderClass = this.findProviderLoader(menuProvider.getClass(), MenuProvider.class);
            providerLoader = (MenuProviderLoader<P>) this.providerLoaders.get(providerLoaderClass);
            if (providerLoader == null) {
                providerLoader = MenuProviderLoader.defaultLoader();
            }

            this.providerLoaderCache.put(menuProvider.getClass(), providerLoader);
        }


        return this.openMenuBuilder(menuProvider, player, providerLoader);
    }

    Class<?> findProviderLoader(Class<?> menuProviderClass, Class<?> providerClass) {
        try {
            Class<?>[] interfaces = menuProviderClass.getInterfaces();
            for (Class<?> menuInterface : interfaces) {
                if (providerClass.isAssignableFrom(menuInterface)) {
                    return menuInterface;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}