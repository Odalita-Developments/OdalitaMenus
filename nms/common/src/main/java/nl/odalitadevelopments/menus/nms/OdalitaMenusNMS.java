package nl.odalitadevelopments.menus.nms;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface OdalitaMenusNMS {

    static @NotNull OdalitaMenusNMS getInstance() {
        return OdalitaMenusNMSInstance.getNms();
    }

    Object itemStackToNMS(ItemStack itemStack);

    ItemStack itemStackFromNMS(Object item);

    Channel getPacketChannel(Player player) throws Exception;

    void sendPacket(Player player, Object packet);

    Object createChatBaseComponent(String string);

    void setInventoryItem(Player player, int slot, ItemStack itemStack, Inventory inventory);

    void changeInventoryTitle(Inventory inventory, String title) throws Exception;

    void setInventoryProperty(Inventory inventory, int propertyIndex, int value);

    void openInventory(Player player, Object inventory, String title) throws Exception;

    Object createAnvilInventory(Player player);

    Object createCraftingInventory(Player player);

    Object createEnchantingInventory(Player player);

    Inventory getInventoryFromNMS(Object nmsInventory);
}