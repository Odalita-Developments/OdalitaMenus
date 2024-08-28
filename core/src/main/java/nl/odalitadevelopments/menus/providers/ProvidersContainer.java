package nl.odalitadevelopments.menus.providers;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.providers.processors.ColorProcessor;
import nl.odalitadevelopments.menus.providers.processors.CooldownProcessor;
import nl.odalitadevelopments.menus.providers.processors.DefaultItemProcessor;
import nl.odalitadevelopments.menus.providers.processors.packet.OdalitaPacketListenerProcessor;
import nl.odalitadevelopments.menus.providers.processors.packet.ProtocolLibPacketListenerProcessor;
import nl.odalitadevelopments.menus.providers.providers.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ProvidersContainer {

    private final OdalitaMenus instance;

    private ColorProvider colorProvider;
    private CooldownProvider cooldownProvider;
    private DefaultItemProvider defaultItemProvider;
    private MenuItemDataProvider menuItemDataProvider;
    private PacketListenerProvider packetListenerProvider;

    public ProvidersContainer(@NotNull OdalitaMenus instance) {
        this.instance = instance;

        this.colorProvider = new ColorProcessor();
        this.cooldownProvider = new CooldownProcessor();
        this.defaultItemProvider = new DefaultItemProcessor();

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.packetListenerProvider = new ProtocolLibPacketListenerProcessor(instance);
        } else {
            this.packetListenerProvider = new OdalitaPacketListenerProcessor(instance);
        }
    }

    public void close(@NotNull OdalitaMenus instance) {
        this.packetListenerProvider.close(instance);
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

    public void setMenuItemDataProvider(@NotNull MenuItemDataProvider menuItemDataProvider) {
        this.menuItemDataProvider = menuItemDataProvider;
    }

    public void setPacketListenerProvider(@NotNull PacketListenerProvider packetListenerProvider) {
        this.packetListenerProvider.close(this.instance);
        this.packetListenerProvider = packetListenerProvider;
    }

    @ApiStatus.Internal
    public @NotNull ColorProvider getColorProvider() {
        return this.colorProvider;
    }

    @ApiStatus.Internal
    public @NotNull CooldownProvider getCooldownProvider() {
        return this.cooldownProvider;
    }

    @ApiStatus.Internal
    public @NotNull DefaultItemProvider getDefaultItemProvider() {
        return this.defaultItemProvider;
    }

    @ApiStatus.Internal
    public @Nullable MenuItemDataProvider getMenuItemDataProvider() {
        return this.menuItemDataProvider;
    }

    @ApiStatus.Internal
    public @NotNull PacketListenerProvider getPacketListenerProvider() {
        return this.packetListenerProvider;
    }
}