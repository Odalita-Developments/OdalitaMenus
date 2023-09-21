package nl.odalitadevelopments.menus.utils;

import io.netty.channel.Channel;
import io.papermc.paper.text.PaperComponents;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.version.ProtocolVersion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static nl.odalitadevelopments.menus.utils.ReflectionUtils.*;

@ApiStatus.Internal
public final class InventoryUtils {

    private InventoryUtils() {
    }

    public static synchronized void updateItem(MenuSession session, int slot, ItemStack itemStack, Inventory inventory) {
        try {
            Object nmsItemStack = GET_NMS_ITEM_STACK.invoke(null, itemStack);

            Object craftInventory = CRAFT_INVENTORY.cast(inventory);
            Object nmsInventory = GET_NMS_INVENTORY.invoke(craftInventory);
            Object contents = GET_NMS_INVENTORY_CONTENTS.invoke(nmsInventory);
            SET_LIST.invoke(contents, slot, nmsItemStack);

            for (Player viewer : session.getViewers()) {
                Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(viewer);
                Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
                int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
                if (windowId <= 0) return;

                Object packetPlayOutSetSlot = createPacketPlayOutSetSlotPacket(windowId, slot, nmsItemStack, activeContainer);
                sendPacket(viewer, packetPlayOutSetSlot);
            }
        } catch (Throwable throwable) {
            session.getInstance().getJavaPlugin().getLogger().log(Level.SEVERE, "Failed to update item in inventory", throwable);
        }
    }

    public static synchronized void changeTitle(MenuSession session, Inventory inventory, String newTitle) {
        try {
            if (session.getViewers().isEmpty()) {
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

            for (Player viewer : session.getViewers()) {
                Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(viewer);
                Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
                int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
                if (windowId <= 0) continue;

                Object nmsInventoryType = GET_NMS_INVENTORY_TYPE.invoke(activeContainer);
                Object packetPlayOutOpenWindow = PACKET_PLAY_OUT_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, nmsInventoryType, titleComponent);

                sendPacket(viewer, packetPlayOutOpenWindow);

                TITLE_FIELD.setAccessible(true);
                TITLE_FIELD.set(activeContainer, titleComponent);
                TITLE_FIELD.setAccessible(false);

                refreshInventory(entityPlayer, activeContainer);
            }
        } catch (Throwable throwable) {
            session.getInstance().getJavaPlugin().getLogger().log(Level.SEVERE, "Failed to change title in inventory", throwable);
        }
    }

    public static synchronized void setProperty(MenuSession session, MenuProperty property, int value) {
        try {
            for (Player viewer : session.getViewers()) {
                Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(viewer);
                Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
                int windowId = WINDOW_ID_FIELD.getInt(activeContainer);
                if (windowId <= 0) continue;

                Object packetPlayOutWindowData = PACKET_PLAY_OUT_WINDOW_DATA_CONSTRUCTOR.newInstance(windowId, property.getIndex(), value);
                sendPacket(viewer, packetPlayOutWindowData);
            }
        } catch (Throwable throwable) {
            session.getInstance().getJavaPlugin().getLogger().log(Level.SEVERE, "Failed to set property in inventory", throwable);
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

    public static Object toNMS(ItemStack itemStack) throws InvocationTargetException, IllegalAccessException {
        return GET_NMS_ITEM_STACK.invoke(null, itemStack);
    }

    public static ItemStack fromNMS(Object item) throws InvocationTargetException, IllegalAccessException {
        return (ItemStack) GET_ITEM_STACK_FROM_NMS.invoke(null, item);
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