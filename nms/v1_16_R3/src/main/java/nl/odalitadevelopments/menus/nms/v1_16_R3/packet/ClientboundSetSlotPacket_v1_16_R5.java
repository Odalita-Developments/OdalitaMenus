package nl.odalitadevelopments.menus.nms.v1_16_R3.packet;

import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import nl.odalitadevelopments.menus.nms.utils.Reflections;
import nl.odalitadevelopments.menus.nms.v1_16_R3.ObfuscatedNames_v1_16_R5;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ClientboundSetSlotPacket_v1_16_R5 implements ClientboundSetSlotPacket {

    private static final Reflections.FieldAccessor<Integer> WINDOW_ID_FIELD;
    private static final Reflections.FieldAccessor<Integer> SLOT_FIELD;
    private static final Reflections.FieldAccessor<net.minecraft.server.v1_16_R3.ItemStack> ITEM_FIELD;

    static {
        WINDOW_ID_FIELD = Reflections.getField(PacketPlayOutSetSlot.class, int.class, ObfuscatedNames_v1_16_R5.SET_SLOT_PACKET_WINDOW_ID);
        SLOT_FIELD = Reflections.getField(PacketPlayOutSetSlot.class, int.class, ObfuscatedNames_v1_16_R5.SET_SLOT_PACKET_SLOT);
        ITEM_FIELD = Reflections.getField(PacketPlayOutSetSlot.class, net.minecraft.server.v1_16_R3.ItemStack.class, ObfuscatedNames_v1_16_R5.SET_SLOT_PACKET_ITEM);
    }

    private final PacketPlayOutSetSlot packet;
    private final ItemStack item;

    public ClientboundSetSlotPacket_v1_16_R5(PacketPlayOutSetSlot packet) {
        this.packet = packet;

        this.item = CraftItemStack.asBukkitCopy(ITEM_FIELD.get(packet));
    }

    @Override
    public int windowId() {
        return WINDOW_ID_FIELD.get(this.packet);
    }

    @Override
    public int stateId() {
        return -1;
    }

    @Override
    public int slot() {
        return SLOT_FIELD.get(this.packet);
    }

    @Override
    public @NotNull ItemStack item() {
        return this.item;
    }

    @Override
    public void update() {
        ITEM_FIELD.set(this.packet, CraftItemStack.asNMSCopy(this.item));
    }
}