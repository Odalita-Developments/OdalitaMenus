package nl.odalitadevelopments.menus.tasks;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class MenuUpdateTask implements MenuTaskRunnable {

    @Override
    public void run(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session) {
        if (!session.isHasUpdatableItems()) return;

        int updatableItems = 0;

        MenuItem[][] contents = session.getContents();
        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                MenuItem menuItem = contents[row][column];
                if (menuItem == null || !menuItem.isUpdatable() || menuItem.getUpdateTicks() <= 0)
                    continue;

                updatableItems++;

                if (tick % menuItem.getUpdateTicks() == 0) {
                    ItemStack item = menuItem.getItemStack(instance);
                    int slot = SlotPos.of(row, column).getSlot();

                    InventoryUtils.updateItem(player, slot, item, session.getInventory());
                }
            }
        }

        if (updatableItems == 0) {
            session.setHasUpdatableItems(false);
        }
    }
}