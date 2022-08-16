package nl.tritewolf.tritemenus.providers;

import nl.tritewolf.tritemenus.providers.processors.ColorProcessor;
import nl.tritewolf.tritemenus.providers.processors.DefaultItemProcessor;
import nl.tritewolf.tritemenus.providers.providers.ColorProvider;
import nl.tritewolf.tritemenus.providers.providers.DefaultItemProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class ProvidersContainer {

    private ColorProvider colorProvider;
    private DefaultItemProvider defaultItemProvider;

    public ProvidersContainer() {
        this.colorProvider = new ColorProcessor();
        this.defaultItemProvider = new DefaultItemProcessor();
    }

    public void setColorProvider(@NotNull ColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public void setDefaultItemProvider(@NotNull DefaultItemProvider defaultItemProvider) {
        this.defaultItemProvider = defaultItemProvider;
    }

    @ApiStatus.Internal
    public ColorProvider getColorProvider() {
        return colorProvider;
    }

    @ApiStatus.Internal
    public DefaultItemProvider getDefaultItemProvider() {
        return defaultItemProvider;
    }
}