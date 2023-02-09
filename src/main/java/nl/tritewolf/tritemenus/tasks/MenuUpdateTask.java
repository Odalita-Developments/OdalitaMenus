package nl.tritewolf.tritemenus.tasks;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class MenuUpdateTask implements MenuTaskRunnable {

    @Override
    public void run(@NotNull TriteMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session) {
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