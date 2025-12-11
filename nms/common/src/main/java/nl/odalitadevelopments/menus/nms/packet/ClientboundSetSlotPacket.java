package nl.odalitadevelopments.menus.nms.packet;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ClientboundSetSlotPacket {

    int windowId();

    int stateId();

    int slot();

    @NotNull
    ItemStack item();

    Object update();
}