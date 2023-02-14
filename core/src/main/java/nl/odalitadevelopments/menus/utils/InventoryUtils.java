package nl.odalitadevelopments.menus.utils;

import nl.odalitadevelopments.menus.utils.version.ProtocolVersion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

import static nl.odalitadevelopments.menus.utils.ReflectionUtils.*;

@ApiStatus.Internal
public final class InventoryUtils {

    private InventoryUtils() {
    }

    public static synchronized void updateItem(Player player, int slot, ItemStack itemStack, Inventory inventory) {
        try {
            Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
            Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
            int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
            if (windowId <= 0) return;

            Object nmsItemStack = GET_NMS_ITEM_STACK.invoke(null, itemStack);

            Object craftInventory = CRAFT_INVENTORY.cast(inventory);
            Object nmsInventory = GET_NMS_INVENTORY.invoke(craftInventory);
            Object contents = GET_NMS_INVENTORY_CONTENTS.invoke(nmsInventory);
            SET_LIST.invoke(contents, slot, nmsItemStack);

            Object packetPlayOutSetSlot;
            if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
                // From 1.17.1 it is required to add a 'stateId' as parameter to the packet
                Object stateId = WINDOW_STATE_ID_METHOD.invoke(activeContainer);
                packetPlayOutSetSlot = PACKET_PLAY_OUT_SET_SLOT_CONSTRUCTOR.newInstance(windowId, stateId, slot, nmsItemStack);
            } else {
                packetPlayOutSetSlot = PACKET_PLAY_OUT_SET_SLOT_CONSTRUCTOR.newInstance(windowId, slot, nmsItemStack);
            }

            sendPacket(player, packetPlayOutSetSlot);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static synchronized void changeTitle(Inventory inventory, String newTitle) {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (!(viewer instanceof Player player)) continue;

            try {
                Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
                Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
                int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
                if (windowId <= 0) return;

                Object nmsInventoryType = GET_NMS_INVENTORY_TYPE.invoke(activeContainer);
                Object titleComponent = createChatBaseComponent(newTitle);

                Object packetPlayOutOpenWindow = PACKET_PLAY_OUT_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, nmsInventoryType, titleComponent);

                sendPacket(player, packetPlayOutOpenWindow);

                TITLE_FIELD.setAccessible(true);
                TITLE_FIELD.set(activeContainer, titleComponent);
                TITLE_FIELD.setAccessible(false);

                if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17)) {
                    REFRESH_INVENTORY.invoke(activeContainer);
                } else if (ProtocolVersion.getServerVersion().isEqual(ProtocolVersion.MINECRAFT_1_16_5)) {
                    Object items = GET_NMS_CONTAINER_ITEMS_1165.get(activeContainer);
                    REFRESH_INVENTORY_1165.invoke(entityPlayer, activeContainer, items);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static ItemStack createItemStack(Material material, String displayName, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String loreLine : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', loreLine));
            }

            itemMeta.setLore(loreList);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}