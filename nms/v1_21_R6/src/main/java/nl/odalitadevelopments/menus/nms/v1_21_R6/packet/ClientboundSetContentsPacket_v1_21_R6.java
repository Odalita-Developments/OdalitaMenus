package nl.odalitadevelopments.menus.nms.v1_21_R6.packet;

import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundSetContentsPacket_v1_21_R6 implements ClientboundSetContentsPacket {

    private final ClientboundContainerSetContentPacket packet;
    private final List<ItemStack> items;
    private final ItemStack carriedItem;

    public ClientboundSetContentsPacket_v1_21_R6(ClientboundContainerSetContentPacket packet) {
        this.packet = packet;

        List<ItemStack> items = new ArrayList<>();
        for (net.minecraft.world.item.ItemStack itemStack : packet.items()) {
            items.add(CraftItemStack.asBukkitCopy(itemStack));
        }

        this.items = items;
        this.carriedItem = CraftItemStack.asBukkitCopy(packet.carriedItem());
    }

    @Override
    public int windowId() {
        return this.packet.containerId();
    }

    @Override
    public int stateId() {
        return this.packet.stateId();
    }

    @Override
    public @NotNull List<ItemStack> items() {
        return this.items;
    }

    @Override
    public @Nullable ItemStack carriedItem() {
        return this.carriedItem;
    }

    @Override
    public Object update() {
        List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : this.items) {
            items.add(CraftItemStack.asNMSCopy(itemStack));
        }

        return new ClientboundContainerSetContentPacket(
                this.packet.containerId(),
                this.packet.stateId(),
                items,
                CraftItemStack.asNMSCopy(this.carriedItem)
        );
    }
}