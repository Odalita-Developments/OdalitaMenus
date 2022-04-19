package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.tritewolf.tritemenus.utils.ReflectionUtils.*;

public final class TriteMenuUpdateTask implements Runnable {

    @TriteJect
    private TriteMenuProcessor triteMenuProcessor;

    private static final AtomicInteger TICKS = new AtomicInteger(0);

    @Override
    public void run() {
        int ticks = TICKS.incrementAndGet();

        for (Map.Entry<UUID, Map<Class<?>, Pair<TriteMenuProvider, TriteMenuObject>>> menus : triteMenuProcessor.getMenus().entrySet()) {
            Pair<TriteMenuProvider, TriteMenuObject> triteMenuObjectPair = menus.getValue().values().stream().filter(menuObject -> menuObject.getValue().isHasUpdatableItems() && menuObject.getValue().isHasMenuOpened()).findFirst().orElse(null);

            if (triteMenuObjectPair == null) continue;

            Player player = Bukkit.getPlayer(menus.getKey());
            if (player == null || !player.isOnline()) continue;

            TriteMenuItem[][] contents = triteMenuObjectPair.getValue().getContents();
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[0].length; column++) {
                    TriteMenuItem triteMenuItem = contents[row][column];
                    if (triteMenuItem == null || !triteMenuItem.isUpdatable()) continue;

                    if (ticks % triteMenuItem.getUpdateTicks() == 0) {
                        ItemStack item = triteMenuItem.getItemStack();
                        int slot = 9 + row + column;

                        this.updateItem(player, slot, item, triteMenuObjectPair.getValue().getInventory());
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