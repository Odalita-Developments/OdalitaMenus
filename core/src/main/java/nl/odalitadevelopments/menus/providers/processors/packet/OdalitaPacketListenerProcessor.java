package nl.odalitadevelopments.menus.providers.processors.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
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

    private static final Map<OdalitaMenus, Map<ServerboundPacketType, BiFunction<Player, Object, Boolean>>> packetListenersServerbound = new HashMap<>();

    private final OdalitaMenus instance;

    public OdalitaPacketListenerProcessor(OdalitaMenus instance) {
        this.instance = instance;
        packetListenersServerbound.put(instance, new HashMap<>());

        Bukkit.getPluginManager().registerEvents(this, instance.getJavaPlugin());
    }

    @Override
    public void close(@NotNull OdalitaMenus instance) {
        packetListenersServerbound.remove(instance);
        HandlerList.unregisterAll(this);

        if (packetListenersServerbound.isEmpty()) {
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
    public void interceptServerbound(@NotNull ServerboundPacketType serverboundPacketType, @NotNull BiFunction<@NotNull Player, @NotNull Object, @NotNull Boolean> packetFunction) {
        packetListenersServerbound.get(this.instance).put(serverboundPacketType, packetFunction);
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
            public void channelRead(ChannelHandlerContext ctx, Object packetObject) throws Exception {
                String packetClassName = packetObject.getClass().getSimpleName();
                ServerboundPacketType serverboundPacketType = getPacketType(packetClassName);
                if (serverboundPacketType == null) {
                    super.channelRead(ctx, packetObject);
                    return;
                }

                for (Map<ServerboundPacketType, BiFunction<Player, Object, Boolean>> map : packetListenersServerbound.values()) {
                    BiFunction<Player, Object, Boolean> function = map.get(serverboundPacketType);
                    if (function != null && function.apply(player, packetObject)) {
                        // If function returns true, cancel the packet
                        return;
                    }
                }

                super.channelRead(ctx, packetObject);
            }
        };
    }

    private ServerboundPacketType getPacketType(String packetClassName) {
        if (packetClassName.equals("PacketPlayInWindowClick")) {
            return ServerboundPacketType.CLICK_WINDOW;
        }

        return null;
    }
}