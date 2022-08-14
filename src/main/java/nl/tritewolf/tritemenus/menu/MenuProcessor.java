package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.providers.MenuProviderLoader;
import nl.tritewolf.tritemenus.menu.type.SupportedMenuTypes;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class MenuProcessor {

    @TriteJect
    @Getter(AccessLevel.PACKAGE)
    private ItemProcessor itemProcessor;

    @TriteJect
    @Getter(AccessLevel.PACKAGE)
    private SupportedMenuTypes supportedMenuTypes;

    private final Map<Class<? extends MenuProvider>, MenuProviderLoader<?>> providerLoaders = new ConcurrentHashMap<>();
    private final Map<Player, MenuSession> openMenus = new ConcurrentHashMap<>();

    public <P extends MenuProvider> void registerProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuProviderLoader<P> loader) {
        this.providerLoaders.put(providerClass, loader);
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
        MenuProviderLoader<P> providerLoader = null;

        try {
            Class<?>[] interfaces = menuProvider.getClass().getInterfaces();
            for (Class<?> menuInterface : interfaces) {
                if (MenuProvider.class.isAssignableFrom(menuInterface)) {
                    providerLoader = (MenuProviderLoader<P>) this.providerLoaders.get(menuInterface);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (providerLoader == null) {
            providerLoader = MenuProviderLoader.defaultLoader();
        }

        return this.openMenuBuilder(menuProvider, player, providerLoader);
    }
}