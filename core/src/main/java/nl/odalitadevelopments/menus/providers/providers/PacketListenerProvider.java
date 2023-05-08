package nl.odalitadevelopments.menus.providers.providers;

import nl.odalitadevelopments.menus.OdalitaMenus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface PacketListenerProvider {

    void interceptServerbound(@NotNull ServerboundPacketType serverboundPacketType, @NotNull BiFunction<@NotNull Player, @NotNull Object, @NotNull Boolean> packetFunction);

    default void close(@NotNull OdalitaMenus instance) {
    }

    enum ServerboundPacketType {

        CLICK_WINDOW
    }
}