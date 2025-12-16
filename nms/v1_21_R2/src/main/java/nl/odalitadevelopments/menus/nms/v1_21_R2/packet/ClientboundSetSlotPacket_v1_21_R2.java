package nl.odalitadevelopments.menus.nms.v1_21_R2.packet;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import nl.odalitadevelopments.menus.nms.utils.Reflections;
import nl.odalitadevelopments.menus.nms.v1_21_R2.ObfuscatedNames_v1_21_R2;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ClientboundSetSlotPacket_v1_21_R2 implements ClientboundSetSlotPacket {

    private static final Reflections.FieldAccessor<net.minecraft.world.item.ItemStack> ITEM_FIELD;

    static {
        ITEM_FIELD = Reflections.getField(ClientboundContainerSetSlotPacket.class, net.minecraft.world.item.ItemStack.class, ObfuscatedNames_v1_21_R2.SET_SLOT_PACKET_ITEM);
    }

    private final ClientboundContainerSetSlotPacket packet;
    private final ItemStack item;

    public ClientboundSetSlotPacket_v1_21_R2(ClientboundContainerSetSlotPacket packet) {
        this.packet = packet;

        this.item = CraftItemStack.asBukkitCopy(packet.getItem());
    }

    @Override
    public int windowId() {
        return this.packet.getContainerId();
    }

    @Override
    public int stateId() {
        return this.packet.getStateId();
    }

    @Override
    public int slot() {
        return this.packet.getSlot();
    }

    @Override
    public @NotNull ItemStack item() {
        return this.item;
    }

    @Override
    public Object update() {
        ITEM_FIELD.set(this.packet, CraftItemStack.asNMSCopy(this.item));
        return this.packet;
    }
}