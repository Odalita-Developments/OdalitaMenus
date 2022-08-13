package nl.tritewolf.tritemenus.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import nl.tritewolf.tritemenus.utils.callback.Callback;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class Pagination {

    private final String id;
    private final InventoryContents contents;
    private final int itemsPerPage;
    private final MenuIterator iterator;

    private final Map<Integer, Supplier<MenuItem>[]> itemIndex = new HashMap<>();
    private int index = 0;
    private int pageIndex = 0;

    @Setter
    private int currentPage = 0;

    @Setter
    private volatile boolean initialized = false;

    public Pagination(String id, InventoryContents contents, int itemsPerPage, MenuIterator iterator, @NotNull List<Supplier<MenuItem>> items) {
        this.id = id;
        this.contents = contents;
        this.itemsPerPage = itemsPerPage;
        this.iterator = iterator;

        items.forEach(this::addItem);
    }

    private boolean isOnPage() {
        return index < itemsPerPage;
    }

    public boolean isFirst() {
        return this.currentPage == 0;
    }

    public boolean isLast() {
        return itemIndex.get(currentPage + 1) == null;
    }

    public Supplier<MenuItem>[] getItemsOnPage() {
        return itemIndex.get(currentPage);
    }

    public synchronized Pagination addItem(Supplier<MenuItem> menuItemSupplier) {
        if (this.initialized && this.isOnPage() && pageIndex == currentPage) {
            this.iterator.setNextAsync(menuItemSupplier.get());
        }

        if (!isOnPage()) {
            index = 0;
            pageIndex++;
        }

        Supplier<MenuItem>[] items = this.itemIndex.computeIfAbsent(pageIndex, integer -> new Supplier[itemsPerPage]);
        items[index] = menuItemSupplier;
        index++;

        if (this.itemIndex.get(currentPage + 1) != null) {
            MenuObject menuObject = contents.getMenuSession();
            Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = menuObject.getPageSwitchUpdateItems();

            pageSwitchUpdateItems.forEach((slot, item) -> {
                this.contents.set(slot, item.get());
                InventoryUtils.updateItem(menuObject.getPlayer(), slot, item.get().getItemStack(), menuObject.getInventory());
            });
        }

        return this;
    }

    public Pagination nextPage(Callback callback) {
        openPage(++currentPage, callback);
        return this;
    }

    public Pagination nextPage() {
        this.nextPage(() -> {
        });
        return this;
    }

    public Pagination previousPage(Callback callback) {
        openPage(--currentPage, callback);
        return this;
    }

    public Pagination previousPage() {
        previousPage(() -> {
        });
        return this;
    }

    public void openPage(int page, Callback callback) {
        currentPage = page;
        MenuObject menuObject = this.contents.getMenuSession();
        MenuIterator iterator = this.iterator.reset();

        Supplier<MenuItem>[] itemsOnPage = getItemsOnPage();
        List<Integer> reusableItems = new ArrayList<>();
        for (Supplier<MenuItem> menuItemSupplier : itemsOnPage) {

            if (menuItemSupplier == null) {
                int slot = iterator.getSlot();
                reusableItems.add(slot);
                iterator.setNext(DisplayItem.of(new ItemStack(Material.AIR)));

                InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
                continue;
            }


            MenuItem menuItem = menuItemSupplier.get();
            int slot = iterator.getSlot();
            iterator.setNext(menuItem);

            InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItem.getItemStack(), menuObject.getInventory());

        }
        reusableItems.forEach(iterator::addReusableSlot);

        Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = this.contents.getMenuSession().getPageSwitchUpdateItems();
        pageSwitchUpdateItems.forEach((slot, item) -> {
            this.contents.set(slot, item.get());
            InventoryUtils.updateItem(menuObject.getPlayer(), slot, item.get().getItemStack(), menuObject.getInventory());
        });

        callback.callback();
    }
}
