package nl.odalitadevelopments.menus.providers;

import nl.odalitadevelopments.menus.providers.processors.ColorProcessor;
import nl.odalitadevelopments.menus.providers.processors.CooldownProcessor;
import nl.odalitadevelopments.menus.providers.processors.DefaultItemProcessor;
import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import nl.odalitadevelopments.menus.providers.providers.CooldownProvider;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class ProvidersContainer {

    private ColorProvider colorProvider;
    private CooldownProvider cooldownProvider;
    private DefaultItemProvider defaultItemProvider;

    public ProvidersContainer() {
        this.colorProvider = new ColorProcessor();
        this.cooldownProvider = new CooldownProcessor();
        this.defaultItemProvider = new DefaultItemProcessor();
    }

    public void setColorProvider(@NotNull ColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public void setCooldownProvider(@NotNull CooldownProvider cooldownProvider) {
        this.cooldownProvider = cooldownProvider;
    }

    public void setDefaultItemProvider(@NotNull DefaultItemProvider defaultItemProvider) {
        this.defaultItemProvider = defaultItemProvider;
    }

    @ApiStatus.Internal
    public ColorProvider getColorProvider() {
        return colorProvider;
    }

    @ApiStatus.Internal
    public CooldownProvider getCooldownProvider() {
        return cooldownProvider;
    }

    @ApiStatus.Internal
    public DefaultItemProvider getDefaultItemProvider() {
        return defaultItemProvider;
    }
}