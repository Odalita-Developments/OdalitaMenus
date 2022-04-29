package nl.tritewolf.tritemenus.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.MenuObject;
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
        System.out.println(currentPage + " / " + (itemIndex.size() - 1));
        return this.currentPage >= (itemIndex.size() - 1);
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
        MenuObject menuObject = this.contents.getTriteMenu();
        MenuIterator iterator = this.iterator.reset();

        Supplier<MenuItem>[] itemsOnPage = getItemsOnPage();
        for (Supplier<MenuItem> menuItemSupplier : itemsOnPage) {

            if (menuItemSupplier == null) {
                int slot = iterator.getSlot();
                iterator.setNext(new DisplayItem(new ItemStack(Material.AIR)));

                menuObject.getInventory().setItem(slot, new ItemStack(Material.AIR));
                continue;
            }

            MenuItem menuItem = menuItemSupplier.get();
            int slot = iterator.getSlot();
            iterator.setNext(menuItem);

            menuObject.getInventory().setItem(slot, menuItem.getItemStack());
        }

        callback.callback();
    }
}
