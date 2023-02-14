package nl.odalitadevelopments.menus.tasks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import nl.odalitadevelopments.menus.utils.collection.Table;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class MenuUpdateTask implements MenuTaskRunnable {

    private final Table<MenuSession, Integer, UpdatableItemData> updatableItems = new Table<>();

    @Override
    public void runGlobally(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick) {
        for (MenuSession menuSession : this.updatableItems.getRowMap().keySet()) {
            if (menuSession.isClosed()) {
                this.updatableItems.getRowMap().remove(menuSession);
            }
        }
    }

    @Override
    public void runPerSession(@NotNull OdalitaMenus instance, @NotNull MenuProcessor menuProcessor, int tick, @NotNull Player player, @NotNull MenuSession session) {
        if (!session.isHasUpdatableItems()) return;

        int updatableItems = 0;

        MenuItem[][] contents = session.getContents();
        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                MenuItem menuItem = contents[row][column];
                if (menuItem == null || !menuItem.isUpdatable() || menuItem.getUpdateTicks() <= 0)
                    continue;

                updatableItems++;

                UpdatableItemData updatableItemData = this.updatableItems.computeIfAbsent(session, menuItem.getId(), (menuSession, id) -> new UpdatableItemData(menuItem));
                if (updatableItemData.getUpdatedAtTick() == -1 || tick - updatableItemData.getUpdatedAtTick() == menuItem.getUpdateTicks()) {
                    updatableItemData.setUpdatedAtTick(tick);

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

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static final class UpdatableItemData {

        private final MenuItem menuItem;
        private int updatedAtTick = -1;
    }
}