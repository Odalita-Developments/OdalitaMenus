package nl.odalitadevelopments.menus.menu;

import lombok.Getter;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MenuFrameProcessor {

    private final MenuProcessor menuProcessor;

    @Getter
    private final Map<Class<? extends MenuFrameProvider>, MenuFrameProviderLoader<?, ?>> frameProviderLoaders = new ConcurrentHashMap<>();
    private final Map<Class<? extends MenuFrameProvider>, MenuFrameProviderLoader<?, ?>> frameProviderLoaderCache = new HashMap<>();

    MenuFrameProcessor(MenuProcessor menuProcessor) {
        this.menuProcessor = menuProcessor;
    }

    public <P extends MenuFrameProvider> void registerFrameProviderLoader(@NotNull Class<P> providerClass, @NotNull MenuFrameProviderLoader<P, ?> loader) {
        if (!providerClass.isInterface()) {
            throw new IllegalArgumentException("Frame menu providers must be an interface");
        }

        MenuFrameProviderLoader<?, ?> previous = this.frameProviderLoaders.putIfAbsent(providerClass, loader);
        if (previous != null) {
            throw new IllegalStateException("Frame provider loader for '" + providerClass.getName() + "' already registered");
        }
    }

    public <P extends MenuFrameProvider> boolean isFrameProviderLoaderRegistered(@NotNull Class<P> providerClass) {
        return this.frameProviderLoaders.containsKey(providerClass);
    }

    @SuppressWarnings("unchecked")
    public <P extends MenuFrameProvider> @NotNull MenuFrameProviderLoader<P, ?> getFrameProviderLoader(@NotNull P menuProvider) {
        MenuFrameProviderLoader<P, ?> providerLoader = (MenuFrameProviderLoader<P, ?>) this.frameProviderLoaderCache.get(menuProvider.getClass());
        if (providerLoader == null) {
            Class<?> providerLoaderClass = this.menuProcessor.findProviderLoader(menuProvider.getClass(), MenuFrameProvider.class);
            providerLoader = (MenuFrameProviderLoader<P, ?>) this.frameProviderLoaders.get(providerLoaderClass);
            if (providerLoader == null) {
                providerLoader = MenuFrameProviderLoader.defaultLoader();
            }

            this.frameProviderLoaderCache.put(menuProvider.getClass(), providerLoader);
        }

        return providerLoader;
    }
}