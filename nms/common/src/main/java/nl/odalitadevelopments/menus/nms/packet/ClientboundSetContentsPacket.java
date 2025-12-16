package nl.odalitadevelopments.menus.nms.packet;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClientboundSetContentsPacket {

    int windowId();

    int stateId();

    @NotNull
    List<@NotNull ItemStack> items();

    @Nullable
    ItemStack carriedItem();

    Object update();
}