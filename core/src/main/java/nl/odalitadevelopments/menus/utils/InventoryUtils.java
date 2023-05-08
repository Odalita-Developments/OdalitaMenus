package nl.odalitadevelopments.menus.utils;

import io.netty.channel.Channel;
import io.papermc.paper.text.PaperComponents;
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

            Object packetPlayOutSetSlot = createPacketPlayOutSetSlotPacket(windowId, slot, nmsItemStack, activeContainer);
            sendPacket(player, packetPlayOutSetSlot);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static synchronized void updateItemPlayerInventory(Player player, int slot, ItemStack itemStack, boolean updateServer) {
        if (slot < 9) {
            slot += 36;
        } else if (slot > 39) {
            slot += 5; // Offhand
        } else if (slot > 35) {
            slot = 8 - (slot - 36);
        }

        try {
            Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
            Object playerContainer = PLAYER_CONTAINER_FIELD.get(entityPlayer);
            int windowId = WINDOW_ID_FIELD.getInt(playerContainer);

            Object nmsItemStack = GET_NMS_ITEM_STACK.invoke(null, itemStack);

            if (updateServer) {
                Object craftInventory = CRAFT_INVENTORY.cast(player.getInventory());
                Object nmsInventory = GET_NMS_INVENTORY.invoke(craftInventory);
                Object contents = GET_NMS_INVENTORY_CONTENTS.invoke(nmsInventory);
                SET_LIST.invoke(contents, slot, nmsItemStack);
            }

            Object packetPlayOutSetSlot;
            if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
                // From 1.17.1 it is required to add a 'stateId' as parameter to the packet
                Object stateId = WINDOW_STATE_ID_METHOD.invoke(playerContainer);
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
        try {
            if (inventory.getViewers().isEmpty()) {
                Object craftInventory = CRAFT_INVENTORY.cast(inventory);
                Object nmsInventory = GET_NMS_INVENTORY.invoke(craftInventory);
                Object minecraftInventory = MINECRAFT_INVENTORY.cast(nmsInventory);

                MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, newTitle);
                MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);

                if (IS_PAPER) {
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.set(minecraftInventory, PaperComponents.plainSerializer().deserialize(newTitle));
                    PAPER_MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(false);
                }
                return;
            }

            Object titleComponent = createChatBaseComponent(newTitle);

            for (HumanEntity viewer : inventory.getViewers()) {
                if (!(viewer instanceof Player player)) continue;

                Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
                Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
                int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
                if (windowId <= 0) return;

                Object nmsInventoryType = GET_NMS_INVENTORY_TYPE.invoke(activeContainer);
                Object packetPlayOutOpenWindow = PACKET_PLAY_OUT_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, nmsInventoryType, titleComponent);

                sendPacket(player, packetPlayOutOpenWindow);

                TITLE_FIELD.setAccessible(true);
                TITLE_FIELD.set(activeContainer, titleComponent);
                TITLE_FIELD.setAccessible(false);

                refreshInventory(entityPlayer, activeContainer);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static Channel getPacketChannel(Player player) {
        try {
            Object networkManager = getNetworkManager(player);

            Object channel;
            if (ProtocolVersion.getServerVersion().isLowerOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
                channel = NETWORK_MANAGER.getField("channel").get(networkManager);
            } else if (ProtocolVersion.getServerVersion().isEqual(ProtocolVersion.MINECRAFT_1_18_1)) {
                channel = NETWORK_MANAGER.getField("k").get(networkManager);
            } else {
                channel = NETWORK_MANAGER.getField("m").get(networkManager);
            }

            return (Channel) channel;
        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
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