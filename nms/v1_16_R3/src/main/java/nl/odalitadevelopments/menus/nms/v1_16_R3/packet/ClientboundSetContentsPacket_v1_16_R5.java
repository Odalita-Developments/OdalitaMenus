package nl.odalitadevelopments.menus.nms.v1_16_R3.packet;

import net.minecraft.server.v1_16_R3.PacketPlayOutWindowItems;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.nms.utils.Reflections;
import nl.odalitadevelopments.menus.nms.v1_16_R3.ObfuscatedNames_v1_16_R5;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundSetContentsPacket_v1_16_R5 implements ClientboundSetContentsPacket {

    private static final Reflections.FieldAccessor<Integer> WINDOW_ID_FIELD;
    private static final Reflections.FieldAccessor<List<net.minecraft.server.v1_16_R3.ItemStack>> ITEMS_FIELD;

    static {
        WINDOW_ID_FIELD = Reflections.getField(PacketPlayOutWindowItems.class, int.class, ObfuscatedNames_v1_16_R5.SET_CONTENTS_PACKET_WINDOW_ID);
        ITEMS_FIELD = Reflections.getField(PacketPlayOutWindowItems.class, List.class, ObfuscatedNames_v1_16_R5.SET_CONTENTS_PACKET_ITEMS);
    }

    private final PacketPlayOutWindowItems packet;
    private final List<ItemStack> items;

    public ClientboundSetContentsPacket_v1_16_R5(PacketPlayOutWindowItems packet) {
        this.packet = packet;

        List<ItemStack> items = new ArrayList<>();
        for (net.minecraft.server.v1_16_R3.ItemStack itemStack : ITEMS_FIELD.get(packet)) {
            items.add(CraftItemStack.asBukkitCopy(itemStack));
        }

        this.items = items;
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
    public @NotNull List<ItemStack> items() {
        return this.items;
    }

    @Override
    public @Nullable ItemStack carriedItem() {
        return null;
    }

    @Override
    public Object update() {
        List<net.minecraft.server.v1_16_R3.ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : this.items) {
            items.add(CraftItemStack.asNMSCopy(itemStack));
        }

        ITEMS_FIELD.set(this.packet, items);
        return this.packet;
    }
}