package nl.odalitadevelopments.menus.utils.packet;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record OdalitaWindowItemsPacket(int windowId, int stateId, @NotNull List<@NotNull ItemStack> items,
                                       @Nullable ItemStack carriedItem) implements OdalitaMenuPacket {

    public OdalitaWindowItemsPacket(int windowId, @NotNull List<@NotNull ItemStack> items) {
        this(windowId, -1, items, null);
    }
}