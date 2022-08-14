package nl.tritewolf.tritemenus.pagination;

import lombok.Getter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
final class PaginationImpl implements Pagination {

    private final InventoryContents contents;

    private final String id;
    private final int itemsPerPage;
    private final MenuIterator iterator;

    private final Map<Integer, List<Supplier<MenuItem>>> items = new HashMap<>();

    private int currentPage = 0;

    private boolean initialized = false;

    PaginationImpl(InventoryContents contents, String id, int itemsPerPage, MenuIterator iterator) {
        this.contents = contents;
        this.id = id;
        this.itemsPerPage = itemsPerPage;
        this.iterator = iterator;
    }

    @Override
    public int firstPage() {
        return 0;
    }

    @Override
    public int lastPage() {
        return Math.max(0, this.items.size() - 1);
    }

    @Override
    public boolean isFirstPage() {
        return this.currentPage == this.firstPage();
    }

    @Override
    public boolean isLastPage() {
        return this.currentPage == this.lastPage();
    }

    @Override
    public synchronized @NotNull Pagination addItem(@NotNull Supplier<@NotNull MenuItem> itemSupplier) {
        int pageIndex = Math.max(this.items.size() - 1, 0);
        int index = this.items.getOrDefault(pageIndex, new ArrayList<>()).size();

        if (this.initialized && index < this.itemsPerPage && pageIndex == this.currentPage) {
            this.iterator.setNextAsync(itemSupplier.get());
        }

        if (index >= this.itemsPerPage) {
            pageIndex++;
        }

        this.items.computeIfAbsent(pageIndex, integer -> new ArrayList<>())
                .add(itemSupplier);

        if (this.currentPage + 1 <= this.lastPage()) {
            this.contents.menuSession().getCache().getPageSwitchUpdateItems().forEach((slot, item) -> {
                this.contents.setAsync(slot, item.get());
            });
        }

        return this;
    }

    @Override
    public @NotNull Pagination nextPage() {
        return this.open(this.currentPage + 1);
    }

    @Override
    public @NotNull Pagination previousPage() {
        return this.open(this.currentPage - 1);
    }

    @Override
    public @NotNull Pagination open(int page) {
        if (page < 0 || page > this.lastPage()) return this;

        this.iterator.reset();

        List<Supplier<MenuItem>> itemsOnPage = this.getItemsOnPage(page);
        if (itemsOnPage.isEmpty()) return this;

        List<Integer> reusableItems = new ArrayList<>();

        for (Supplier<MenuItem> itemSupplier : itemsOnPage) {
            if (itemSupplier == null) {
                reusableItems.add(this.iterator.getSlot());
                this.iterator.setNextAsync(DisplayItem.of(new ItemStack(Material.AIR)));
                continue;
            }

            this.iterator.setNextAsync(itemSupplier.get());
        }

        reusableItems.forEach(this.iterator::addReusableSlot);

        this.currentPage = page;

        this.contents.menuSession().getCache().getPageSwitchUpdateItems().forEach((slot, item) -> {
            this.contents.setAsync(slot, item.get());
        });

        return this;
    }

    @ApiStatus.Internal
    @Override
    public void setPage(int page) {
        if (this.initialized) return;

        this.currentPage = page;
    }

    @ApiStatus.Internal
    @Override
    public void setInitialized(boolean initialized) {
        if (this.initialized) return;

        this.initialized = initialized;
    }

    @ApiStatus.Internal
    @Override
    public @NotNull List<Supplier<MenuItem>> getItemsOnPage() {
        if (this.initialized) return List.of();

        return this.getItemsOnPage(this.currentPage);
    }

    private List<Supplier<MenuItem>> getItemsOnPage(int page) {
        List<Supplier<MenuItem>> items = this.items.getOrDefault(page, new ArrayList<>());

        for (int i = items.size(); i < this.itemsPerPage; i++) {
            items.add(null);
        }

        return items;
    }
}