package nl.odalitadevelopments.menus.providers.processors.packet;

import io.netty.channel.*;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import nl.odalitadevelopments.menus.utils.packet.PacketConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class OdalitaPacketListenerProcessor implements PacketListenerProvider, Listener {

    private static final String PACKET_HANDLER = "packet_handler";
    private static final String ODALITA_PACKET_HANDLER = "odalita_packet_handler";

    private static final Map<OdalitaMenus, Map<ClientboundPacketType, BiFunction<Player, OdalitaMenuPacket, Boolean>>> packetListenersClientbound = new HashMap<>();

    private final OdalitaMenus instance;

    public OdalitaPacketListenerProcessor(OdalitaMenus instance) {
        this.instance = instance;

        packetListenersClientbound.put(instance, new HashMap<>());

        Bukkit.getPluginManager().registerEvents(this, instance.getJavaPlugin());
    }

    @Override
    public void close(@NotNull OdalitaMenus instance) {
        packetListenersClientbound.remove(instance);

        HandlerList.unregisterAll(this);

        if (packetListenersClientbound.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Channel channel = InventoryUtils.getPacketChannel(player);
                if (channel == null) continue;

                ChannelPipeline pipeline = channel.pipeline();
                if (pipeline == null) continue;

                pipeline.remove(ODALITA_PACKET_HANDLER);
            }
        }
    }

    @Override
    public void interceptClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiFunction<@NotNull Player, @NotNull OdalitaMenuPacket, @NotNull Boolean> packetFunction) {
        packetListenersClientbound.get(this.instance).put(clientboundPacketType, packetFunction);
    }

    @EventHandler
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Channel channel = InventoryUtils.getPacketChannel(event.getPlayer());
        if (channel == null) return;

        ChannelPipeline pipeline = channel.pipeline();
        if (pipeline == null) return;

        if (pipeline.get(ODALITA_PACKET_HANDLER) != null) return;
        pipeline.addBefore(PACKET_HANDLER, ODALITA_PACKET_HANDLER, this.createChannelDuplexHandler(event.getPlayer()));
    }

    private ChannelDuplexHandler createChannelDuplexHandler(Player player) {
        return new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object packetObject, ChannelPromise promise) throws Exception {
                String packetClassName = packetObject.getClass().getSimpleName();
                ClientboundPacketType clientboundPacketType = getClientboundPacketType(packetClassName);
                if (clientboundPacketType == null) {
                    super.write(ctx, packetObject, promise);
                    return;
                }

                OdalitaMenuPacket packet = PacketConverter.convertClientboundPacket(clientboundPacketType, packetObject);
                if (packet == null) {
                    super.write(ctx, packetObject, promise);
                    return;
                }

                for (Map<ClientboundPacketType, BiFunction<Player, OdalitaMenuPacket, Boolean>> map : packetListenersClientbound.values()) {
                    BiFunction<Player, OdalitaMenuPacket, Boolean> function = map.get(clientboundPacketType);
                    if (function != null && function.apply(player, packet)) {
                        // If function returns true, cancel the packet
                        return;
                    }

                    PacketConverter.updateClientboundPacket(clientboundPacketType, packet, packetObject);
                }

                super.write(ctx, packetObject, promise);
            }
        };
    }

    private ClientboundPacketType getClientboundPacketType(String packetClassName) {
        if (packetClassName.equals("PacketPlayOutSetSlot")) {
            return ClientboundPacketType.SET_SLOT;
        }

        if (packetClassName.equals("PacketPlayOutWindowItems")) {
            return ClientboundPacketType.WINDOW_ITEMS;
        }

        return null;
    }
}