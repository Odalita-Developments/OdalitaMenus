package nl.odalitadevelopments.menus.nms;

import io.netty.channel.Channel;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetContentsPacket;
import nl.odalitadevelopments.menus.nms.packet.ClientboundSetSlotPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface OdalitaMenusNMS {

    static @NotNull OdalitaMenusNMS getInstance() {
        return OdalitaMenusNMSInstance.getNms();
    }

    Inventory getTopInventory(InventoryEvent event);

    Channel getPacketChannel(Player player) throws Exception;

    void sendPacket(Player player, Object packet);

    Object createChatBaseComponent(String string);

    void setInventoryItem(Player player, int slot, ItemStack itemStack, Inventory inventory);

    void changeInventoryTitle(Inventory inventory, String title) throws Exception;

    void setInventoryProperty(Inventory inventory, int propertyIndex, int value);

    void openInventory(Player player, Object inventory, String title) throws Exception;

    Object createAnvilInventory(Player player);

    Object createCartographyInventory(Player player);

    Object createCraftingInventory(Player player);

    Object createEnchantingInventory(Player player);

    Object createLoomInventory(Player player);

    Object createSmithingInventory(Player player);

    Object createStonecutterInventory(Player player);

    Inventory getInventoryFromNMS(Object nmsInventory);

    ClientboundSetSlotPacket readSetSlotPacket(Object packet);

    ClientboundSetContentsPacket readSetContentsPacket(Object packet);

    String setSlotPacketName();

    String windowItemsPacketName();
}