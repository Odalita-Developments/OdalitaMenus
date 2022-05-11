package nl.tritewolf.tritemenus.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import static nl.tritewolf.tritemenus.utils.ReflectionUtils.*;

@ApiStatus.Internal
public class InventoryUtils {

    public static synchronized void updateItem(Player player, int slot, ItemStack itemStack, Inventory inventory) {
        try {
            Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
            Object activeContainer = ACTIVE_CONTAINER_FIELD.get(entityPlayer);
            int windowId = WINDOW_ID_FIELD.getInt(activeContainer);

            Object nmsItemStack = GET_NMS_ITEM_STACK.invoke(null, itemStack);

            Object craftInventory = CRAFT_INVENTORY.cast(inventory);
            Object nmsInventory = GET_NMS_INVENTORY.invoke(craftInventory);
            Object contents = GET_NMS_INVENTORY_CONTENTS.invoke(nmsInventory);
            SET_LIST.invoke(contents, slot, nmsItemStack);

            Object packetPlayOutSetSlot = PACKET_PLAY_OUT_SET_SLOT_CONSTRUCTOR.newInstance(windowId, slot, nmsItemStack);
            sendPacket(player, packetPlayOutSetSlot);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}