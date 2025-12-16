package nl.odalitadevelopments.menus.utils.packet.providers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import nl.odalitadevelopments.menus.utils.packet.PacketListenerProvider;
import nl.odalitadevelopments.menus.utils.packet.type.OdalitaSetContentsPacket;
import nl.odalitadevelopments.menus.utils.packet.type.OdalitaSetSlotPacket;
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
    private final ProtocolManager protocolManager;

    private final PacketListener setSlotListener;
    private final PacketListener windowItemsListener;

    public ProtocolLibPacketListenerProcessor(OdalitaMenus instance) {
        this.instance = instance;

        packetListenersClientbound.put(instance, new HashMap<>());

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        this.protocolManager.addPacketListener(this.setSlotListener = this.listenSetSlot());
        this.protocolManager.addPacketListener(this.windowItemsListener = this.listenWindowItems());
    }

    @Override
    public void close() {
        packetListenersClientbound.remove(this.instance);

        this.protocolManager.removePacketListener(this.setSlotListener);
        this.protocolManager.removePacketListener(this.windowItemsListener);
    }

    @Override
    public void listenClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiConsumer<@NotNull Player, @NotNull OdalitaMenuPacket> packetConsumer) {
        packetListenersClientbound.get(this.instance).put(clientboundPacketType, packetConsumer);
    }

    private PacketListener listenSetSlot() {
        return new PacketAdapter(
                instance.getJavaPlugin(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.SET_SLOT
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();

                for (Map<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> map : packetListenersClientbound.values()) {
                    for (Map.Entry<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> entry : map.entrySet()) {
                        if (entry.getKey() == ClientboundPacketType.SET_SLOT) {
                            int windowId = packet.getIntegers().read(0);
                            int stateId = packet.getIntegers().read(1);
                            int slot = packet.getIntegers().read(2);
                            ItemStack item = packet.getItemModifier().read(0);

                            OdalitaSetSlotPacket odalitaSetSlotPacket = new OdalitaSetSlotPacket(null, windowId, stateId, slot, item);
                            entry.getValue().accept(player, odalitaSetSlotPacket);

                            packet.getItemModifier().write(0, odalitaSetSlotPacket.item());
                        }
                    }
                }
            }
        };
    }

    private PacketListener listenWindowItems() {
        return new PacketAdapter(
                instance.getJavaPlugin(),
                ListenerPriority.NORMAL,
                PacketType.Play.Server.WINDOW_ITEMS
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();

                for (Map<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> map : packetListenersClientbound.values()) {
                    for (Map.Entry<ClientboundPacketType, BiConsumer<Player, OdalitaMenuPacket>> entry : map.entrySet()) {
                        if (entry.getKey() == ClientboundPacketType.SET_CONTENTS) {
                            int windowId = packet.getIntegers().read(0);
                            int stateId = packet.getIntegers().read(1);
                            List<ItemStack> items = packet.getItemListModifier().read(0);
                            ItemStack carriedItem = packet.getItemModifier().read(0);

                            OdalitaSetContentsPacket odalitaSetContentsPacket = new OdalitaSetContentsPacket(null, windowId, stateId, items, carriedItem);
                            entry.getValue().accept(player, odalitaSetContentsPacket);

                            packet.getItemListModifier().write(0, odalitaSetContentsPacket.items());
                        }
                    }
                }
            }
        };
    }
}