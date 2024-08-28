package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface PacketListenerProvider {

    void listenClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiConsumer<@NotNull Player, @NotNull OdalitaMenuPacket> packetConsumer);

    default void close(@NotNull OdalitaMenus instance) {
    }

    enum ClientboundPacketType {

        SET_SLOT,
        SET_CONTENTS,
        ;
    }
}