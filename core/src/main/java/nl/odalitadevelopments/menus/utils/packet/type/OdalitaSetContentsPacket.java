package nl.odalitadevelopments.menus.utils.packet.type;

import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.utils.packet.OdalitaMenuPacket;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record OdalitaSetContentsPacket(@Nullable ClientboundSetContentsPacket origin, int windowId, int stateId,
                                       @NotNull List<@NotNull ItemStack> items,
                                       @Nullable ItemStack carriedItem) implements OdalitaMenuPacket {

    public OdalitaSetContentsPacket(@Nullable ClientboundSetContentsPacket origin, int windowId, @NotNull List<@NotNull ItemStack> items) {
        this(origin, windowId, -1, items, null);
    }
}