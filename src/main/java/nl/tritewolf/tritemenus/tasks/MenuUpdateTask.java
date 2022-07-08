package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritejection.annotations.TriteJect;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

            int updatableItems = 0;

            MenuItem[][] contents = menuObject.getContents();
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[0].length; column++) {
                    MenuItem menuItem = contents[row][column];
                    if (menuItem == null || !menuItem.isUpdatable() || menuItem.getUpdateTicks() <= 0)
                        continue;

                    updatableItems++;

                    if (ticks % menuItem.getUpdateTicks() == 0) {
                        ItemStack item = menuItem.getItemStack();
                        int slot = SlotPos.of(row, column).getSlot();

                        InventoryUtils.updateItem(player, slot, item, menuObject.getInventory());
                    }
                }
            }

            if (updatableItems == 0) {
                menuObject.setHasUpdatableItems(false);
            }
        }
    }
}