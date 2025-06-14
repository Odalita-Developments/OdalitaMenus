package nl.odalitadevelopments.menus.nms.v1_21_R4.packet;

import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.nms.utils.Reflections;
import nl.odalitadevelopments.menus.nms.v1_21_R4.ObfuscatedNames_v1_21_R4;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundSetContentsPacket_v1_21_R4 implements ClientboundSetContentsPacket {

//    TODO: this does not work, ClientboundContainerSetContentPacket is now a record and fields cannot be modifier. Fix!
//    private static final Reflections.FieldAccessor<List<net.minecraft.world.item.ItemStack>> ITEMS_FIELD;
//    private static final Reflections.FieldAccessor<net.minecraft.world.item.ItemStack> CARRIED_ITEM_FIELD;

    static {
//        TODO: this does not work, ClientboundContainerSetContentPacket is now a record and fields cannot be modifier. Fix!
//        ITEMS_FIELD = Reflections.getField(ClientboundContainerSetContentPacket.class, List.class, ObfuscatedNames_v1_21_R4.SET_CONTENTS_PACKET_ITEMS);
//        CARRIED_ITEM_FIELD = Reflections.getField(ClientboundContainerSetContentPacket.class, net.minecraft.world.item.ItemStack.class, ObfuscatedNames_v1_21_R4.SET_CONTENTS_PACKET_CARRIED_ITEM);
    }

    private final ClientboundContainerSetContentPacket packet;
    private final List<ItemStack> items;
    private final ItemStack carriedItem;

    public ClientboundSetContentsPacket_v1_21_R4(ClientboundContainerSetContentPacket packet) {
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
    public void update() {
        List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : this.items) {
            items.add(CraftItemStack.asNMSCopy(itemStack));
        }

//        TODO: this does not work, ClientboundContainerSetContentPacket is now a record and fields cannot be modifier. Fix!
//        ITEMS_FIELD.set(this.packet, items);
//        CARRIED_ITEM_FIELD.set(this.packet, CraftItemStack.asNMSCopy(this.carriedItem));
    }
}