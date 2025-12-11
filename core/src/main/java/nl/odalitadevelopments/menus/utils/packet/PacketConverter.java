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
        return switch (type) {
            case SET_SLOT -> convertSetSlotPacket(packetObject);
            case SET_CONTENTS -> convertWindowItemsPacket(packetObject);
        };
    }

    public static Object updateClientboundPacket(@NotNull PacketListenerProvider.ClientboundPacketType type, @NotNull OdalitaMenuPacket packet) {
        return switch (type) {
            case SET_SLOT -> updateSetSlotPacket(packet);
            case SET_CONTENTS -> updateWindowItemsPacket(packet);
        };
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

    private static Object updateSetSlotPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket) {
        if (!(odalitaMenuPacket instanceof OdalitaSetSlotPacket packet)) return null;

        return packet.origin() != null ? packet.origin().update() : null;
    }

    private static Object updateWindowItemsPacket(@NotNull OdalitaMenuPacket odalitaMenuPacket) {
        if (!(odalitaMenuPacket instanceof OdalitaSetContentsPacket packet)) return null;

        return packet.origin() != null ? packet.origin().update() : null;
    }
}