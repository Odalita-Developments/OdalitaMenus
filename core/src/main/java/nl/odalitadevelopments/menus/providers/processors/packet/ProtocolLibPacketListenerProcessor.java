package nl.odalitadevelopments.menus.providers.processors.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import nl.odalitadevelopments.menus.utils.packet.OdalitaSetSlotPacket;
import nl.odalitadevelopments.menus.utils.packet.OdalitaWindowItemsPacket;
import nl.odalitadevelopments.menus.utils.version.ProtocolVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class ProtocolLibPacketListenerProcessor implements PacketListenerProvider {

    private static final Map<OdalitaMenus, Map<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>>> packetListenersClientbound = new HashMap<>();

    private final OdalitaMenus instance;

    public ProtocolLibPacketListenerProcessor(OdalitaMenus instance) {
        this.instance = instance;

        packetListenersClientbound.put(instance, new HashMap<>());

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(
                instance.getJavaPlugin(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.SET_SLOT
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();

                for (Map<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> map : packetListenersClientbound.values()) {
                    for (Map.Entry<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> entry : map.entrySet()) {
                        if (entry.getKey() == ClientboundPacketType.SET_SLOT) {
                            int windowId = packet.getIntegers().read(0);
                            int stateId = (is1171) ? -1 : packet.getIntegers().read(1);
                            int slot = packet.getIntegers().read((is1171) ? 2 : 1);
                            ItemStack item = packet.getItemModifier().read(0);

                            OdalitaSetSlotPacket odalitaSetSlotPacket = new OdalitaSetSlotPacket(windowId, stateId, slot, item);
                            entry.getValue().accept(player, odalitaSetSlotPacket);

                            packet.getItemModifier().write(0, odalitaSetSlotPacket.item());
                        }
                    }
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(
                instance.getJavaPlugin(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.WINDOW_ITEMS
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                boolean is1171 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1);

                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();

                for (Map<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> map : packetListenersClientbound.values()) {
                    for (Map.Entry<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> entry : map.entrySet()) {
                        if (entry.getKey() == ClientboundPacketType.WINDOW_ITEMS) {
                            int windowId = packet.getIntegers().read(0);
                            int stateId = (is1171) ? -1 : packet.getIntegers().read(1);
                            List<ItemStack> items = packet.getItemListModifier().read(0);
                            ItemStack carriedItem = packet.getItemModifier().read(0);

                            OdalitaWindowItemsPacket odalitaWindowItemsPacket = new OdalitaWindowItemsPacket(windowId, stateId, items, carriedItem);
                            entry.getValue().accept(player, odalitaWindowItemsPacket);

                            packet.getItemListModifier().write(0, odalitaWindowItemsPacket.items());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void close(@NotNull OdalitaMenus instance) {
        packetListenersClientbound.remove(instance);
    }

    @Override
    public void listenClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiConsumer<@NotNull Player, @NotNull OdalitaMenuPacket> packetConsumer) {
        packetListenersClientbound.get(this.instance).put(clientboundPacketType, packetConsumer);
    }
}