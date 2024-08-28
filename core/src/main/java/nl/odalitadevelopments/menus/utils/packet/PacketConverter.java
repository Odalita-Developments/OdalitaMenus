package nl.odalitadevelopments.menus.utils.packet;

import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import nl.odalitadevelopments.menus.providers.providers.PacketListenerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketConverter {

    private PacketConverter() {
    }

    public static @Nullable OdalitaMenuPacket convertClientboundPacket(@NotNull PacketListenerProvider.ClientboundPacketType type, @NotNull Object packetObject) {
        if (type == PacketListenerProvider.ClientboundPacketType.SET_SLOT) {
            return convertSetSlotPacket(packetObject);
        }

        if (type == PacketListenerProvider.ClientboundPacketType.SET_CONTENTS) {
            return convertWindowItemsPacket(packetObject);
        }

        return null;
    }

    public static void updateClientboundPacket(@NotNull PacketListenerProvider.ClientboundPacketType type, @NotNull OdalitaMenuPacket packet) {
        if (type == PacketListenerProvider.ClientboundPacketType.SET_SLOT) {
            updateSetSlotPacket(packet);
            return;
        }

        if (type == PacketListenerProvider.ClientboundPacketType.SET_CONTENTS) {
            updateWindowItemsPacket(packet);
        }
    }

    private static OdalitaSetSlotPacket convertSetSlotPacket(@NotNull Object packetObject) {
        ClientboundSetSlotPacket packet = OdalitaMenusNMS.getInstance().readSetSlotPacket(packetObject);
        if (packet == null) return null;

        return new OdalitaSetSlotPacket(packet, packet.windowId(), packet.slot(), packet.item());
    }

    private static OdalitaSetContentsPacket convertWindowItemsPacket(@NotNull Object packetObject) {
        ClientboundSetContentsPacket packet = OdalitaMenusNMS.getInstance().readSetContentsPacket(packetObject);
        if (packet == null) return null;

        return new OdalitaSetContentsPacket(packet, packet.windowId(), packet.stateId(), packet.items(), packet.carriedItem());
    }

    private static void updateSetSlotPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket) {
        if (!(odalitaMenuPacket instanceof OdalitaSetSlotPacket packet)) return;

        if (packet.origin() != null) {
            packet.origin().update();
        }
    }

    private static void updateWindowItemsPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket) {
        if (!(odalitaMenuPacket instanceof OdalitaSetContentsPacket packet)) return;

        if (packet.origin() != null) {
            packet.origin().update();
        }
    }
}