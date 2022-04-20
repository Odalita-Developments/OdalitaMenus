package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.tritewolf.tritemenus.utils.ReflectionUtils.*;

public final class MenuUpdateTask implements Runnable {

    @TriteJect
    private MenuProcessor menuProcessor;

    private static final AtomicInteger TICKS = new AtomicInteger(0);

    @Override
    public void run() {
        Map<UUID, MenuObject> openMenus = this.menuProcessor.getOpenMenus();
        if (openMenus.isEmpty()) {
            TICKS.set(0);
            return;
        }

        int ticks = TICKS.incrementAndGet();

        for (Map.Entry<UUID, MenuObject> entry : openMenus.entrySet()) {
            MenuObject menuObject = entry.getValue();
            if (menuObject == null || !menuObject.isHasUpdatableItems()) continue;

            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null || !player.isOnline()) continue;

            MenuItem[][] contents = menuObject.getContents();
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[0].length; column++) {
                    MenuItem menuItem = contents[row][column];
                    if (menuItem == null || !menuItem.isUpdatable() || menuItem.getUpdateTicks() <= 0)
                        continue;

                    if (ticks % menuItem.getUpdateTicks() == 0) {
                        ItemStack item = menuItem.getItemStack();
                        int slot = SlotPos.of(row, column).getSlot();

                        this.updateItem(player, slot, item, menuObject.getInventory());
                    }
                }
            }
        }
    }

    private void updateItem(Player player, int slot, ItemStack itemStack, Inventory inventory) {
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