package nl.odalitadevelopments.menus.utils.packet;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record OdalitaSetSlotPacket(int windowId, int stateId, int slot,
                                   @NotNull ItemStack item) implements OdalitaMenuPacket {

    public OdalitaSetSlotPacket(int windowId, int slot, @NotNull ItemStack item) {
        this(windowId, -1, slot, item);
    }
}