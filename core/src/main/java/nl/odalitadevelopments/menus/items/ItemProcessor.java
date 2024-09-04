package nl.odalitadevelopments.menus.items;

import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.menu.MenuData;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.pagination.ObjectPagination;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ItemProcessor {

    @ApiStatus.Internal
    public void initializeItems(MenuSession menuSession, IMenuContents menuContents) {
        SupportedMenuType menuType = menuSession.data().getOrThrow(MenuData.Key.TYPE);
        Inventory inventory = menuSession.inventory();

        MenuItem[][] contents = menuSession.contents();

        for (IPagination<?, ?> pagination : menuSession.cache().getPaginationMap().values()) {
            if (pagination instanceof ObjectPagination<?>) continue;

            List<Supplier<MenuItem>> itemsOnPage = pagination.getItemsOnPage();
            if (itemsOnPage.isEmpty()) {
                pagination.setInitialized();
                continue;
            }

            for (Supplier<MenuItem> menuItemSupplier : itemsOnPage) {
                if (menuItemSupplier == null) continue;

                MenuItem menuItem = menuItemSupplier.get();
                if (menuItem == null) continue;

                int slot = pagination.iterator().getSlot();
                if (inventory.getItem(slot) != null) continue;

                menuContents.set(slot, menuItem);
                pagination.iterator().next();
            }

            pagination.setInitialized();
        }

        for (Scrollable scrollable : menuSession.cache().getScrollableMap().values()) {
            Map<Integer, Supplier<MenuItem>> pageItems = scrollable.getPageItems();
            if (pageItems.isEmpty()) {
                scrollable.setInitialized();
                continue;
            }

            for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
                int slot = entry.getKey();
                Supplier<MenuItem> menuItemSupplier = entry.getValue();
                if (menuItemSupplier == null) continue;

                MenuItem menuItem = menuItemSupplier.get();
                if (menuItem == null) continue;

                if (inventory.getItem(slot) != null) continue;

                menuContents.set(slot, menuItem);
            }

            scrollable.setInitialized();
        }

        for (int row = 0; row < menuSession.rows(); row++) {
            for (int column = 0; column < menuSession.columns(); column++) {
                MenuItem menuItem = contents[row][column];
                if (menuItem == null) continue;

                int slot = SlotPos.of(menuType.maxRows(), menuType.maxColumns(), row, column).getSlot();
                inventory.setItem(slot, menuItem.provideItem(menuSession.instance(), menuSession));

                if (!menuSession.hasUpdatableItems() && menuItem.isUpdatable()) {
                    menuSession.hasUpdatableItems(true);
                }
            }
        }
    }
}