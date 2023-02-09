package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public record MenuUpdateTask(TriteMenus instance, MenuProcessor menuProcessor) implements Runnable {

    private static final AtomicInteger TICKS = new AtomicInteger(0);

    @Override
    public void run() {
        Map<Player, MenuSession> openMenus = this.menuProcessor.getOpenMenus();
        if (openMenus.isEmpty()) {
            TICKS.set(0);
            return;
        }

        int ticks = TICKS.incrementAndGet();

        for (Map.Entry<Player, MenuSession> entry : openMenus.entrySet()) {
            MenuSession menuSession = entry.getValue();
            if (menuSession == null || !menuSession.isHasUpdatableItems()) continue;

            Player player = entry.getKey();
            if (player == null || !player.isOnline()) continue;

            int updatableItems = 0;

            MenuItem[][] contents = menuSession.getContents();
            for (int row = 0; row < contents.length; row++) {
                for (int column = 0; column < contents[0].length; column++) {
                    MenuItem menuItem = contents[row][column];
                    if (menuItem == null || !menuItem.isUpdatable() || menuItem.getUpdateTicks() <= 0)
                        continue;

                    updatableItems++;

                    if (ticks % menuItem.getUpdateTicks() == 0) {
                        ItemStack item = menuItem.getItemStack(this.instance);
                        int slot = SlotPos.of(row, column).getSlot();

                        InventoryUtils.updateItem(player, slot, item, menuSession.getInventory());
                    }
                }
            }

            if (updatableItems == 0) {
                menuSession.setHasUpdatableItems(false);
            }
        }
    }
}