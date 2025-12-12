package nl.odalitadevelopments.menus.utils.packet;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.utils.packet.providers.OdalitaPacketListenerProcessor;
import nl.odalitadevelopments.menus.utils.packet.providers.ProtocolLibPacketListenerProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@ApiStatus.Internal
public final class OdalitaPacketListener {

    private final PacketListenerProvider packetListenerProvider;

    public OdalitaPacketListener(OdalitaMenus instance) {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.packetListenerProvider = new ProtocolLibPacketListenerProcessor(instance);
        } else {
            this.packetListenerProvider = new OdalitaPacketListenerProcessor(instance);
        }
    }

    public void listenClientbound(@NotNull PacketListenerProvider.ClientboundPacketType clientboundPacketType, @NotNull BiConsumer<@NotNull Player, @NotNull OdalitaMenuPacket> packetConsumer) {
        this.packetListenerProvider.listenClientbound(clientboundPacketType, packetConsumer);
    }

    public void close() {
        this.packetListenerProvider.close();
    }

    public @NotNull PacketListenerProvider getPacketListenerProvider() {
        return this.packetListenerProvider;
    }
}