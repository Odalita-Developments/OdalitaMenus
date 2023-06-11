package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.AbstractMenuIterator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

abstract non-sealed class AbstractPagination<T extends IPagination<T, I>, I extends AbstractMenuIterator<I>> implements IPagination<T, I> {

    protected final MenuContents contents;
    protected final I iterator;

    protected final String id;
    protected final int itemsPerPage;

    protected int currentPage = 0;

    protected boolean initialized = false;

    private final T instance;

    AbstractPagination(MenuContents contents, String id, int itemsPerPage, I iterator) {
        this.instance = this.self();

        this.contents = contents;
        this.id = id;
        this.itemsPerPage = itemsPerPage;
        this.iterator = iterator;
    }

    protected abstract T self();

    @Override
    public final @NotNull String id() {
        return this.id;
    }

    @Override
    public final int itemsPerPage() {
        return this.itemsPerPage;
    }

    @Override
    public final int currentPage() {
        return this.currentPage;
    }

    @Override
    public final int firstPage() {
        return 0;
    }

    @Override
    public final boolean isFirstPage() {
        return this.currentPage == this.firstPage();
    }

    @Override
    public final boolean isLastPage() {
        return this.currentPage == this.lastPage();
    }

    @Override
    public final @NotNull T nextPage() {
        return this.open(this.currentPage + 1);
    }

    @Override
    public final @NotNull T previousPage() {
        return this.open(this.currentPage - 1);
    }

    @Override
    public final @NotNull T open(int page) {
        if (page < 0 || page > this.lastPage()) return this.instance;

        this.iterator.reset();

        List<Supplier<MenuItem>> itemsOnPage = this.getItemsOnPage(page);
        if (itemsOnPage.isEmpty()) return this.instance;

        List<Integer> reusableItems = new ArrayList<>();

        for (Supplier<MenuItem> itemSupplier : itemsOnPage) {
            if (itemSupplier == null) {
                reusableItems.add(this.iterator.getSlot());
                int slot = this.iterator.next();
                this.contents.set(slot, DisplayItem.of(new ItemStack(Material.AIR)));
                continue;
            }

            int slot = this.iterator.next();
            this.contents.set(slot, itemSupplier.get());
        }

        reusableItems.forEach(this.iterator::addReusableSlot);

        this.currentPage = page;

        this.contents.cache().getPageSwitchUpdateItems().forEach(this.contents::set);

        return this.instance;
    }

    protected abstract List<Supplier<MenuItem>> getItemsOnPage(int page);

    @ApiStatus.Internal
    @Override
    public final void setPage(int page) {
        if (this.initialized) return;

        this.currentPage = page;
    }

    @ApiStatus.Internal
    @Override
    public final void setInitialized() {
        this.initialized = true;
    }
}