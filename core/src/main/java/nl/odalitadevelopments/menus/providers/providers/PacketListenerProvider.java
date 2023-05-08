package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface PacketListenerProvider {

    void interceptClientbound(@NotNull ClientboundPacketType clientboundPacketType, @NotNull BiFunction<@NotNull Player, @NotNull OdalitaMenuPacket, @NotNull Boolean> packetFunction);

    default void close(@NotNull OdalitaMenus instance) {
    }

    enum ClientboundPacketType {

        SET_SLOT,
        WINDOW_ITEMS;
    }
}