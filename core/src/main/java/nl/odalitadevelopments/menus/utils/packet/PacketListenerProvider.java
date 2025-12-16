package nl.odalitadevelopments.menus.utils.packet;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@ApiStatus.Internal
public interface PacketListenerProvider {

    void listenClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiConsumer<@NotNull Player, @NotNull OdalitaMenuPacket> packetConsumer);

    default void close() {
    }

    enum ClientboundPacketType {

        SET_SLOT,
        SET_CONTENTS,
        ;
    }
}