package nl.odalitadevelopments.menus.utils.packet;

import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record OdalitaSetSlotPacket(@Nullable ClientboundSetSlotPacket origin, int windowId, int stateId, int slot,
                                   @NotNull ItemStack item) implements OdalitaMenuPacket {

    public OdalitaSetSlotPacket(@Nullable ClientboundSetSlotPacket origin, int windowId, int slot, @NotNull ItemStack item) {
        this(origin, windowId, -1, slot, item);
    }
}