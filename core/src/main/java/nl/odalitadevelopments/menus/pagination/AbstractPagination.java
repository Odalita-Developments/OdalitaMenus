package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.AbstractMenuIterator;
import nl.odalitadevelopments.menus.utils.BukkitThreadHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

abstract non-sealed class AbstractPagination<T extends IPagination<T, I>, I extends AbstractMenuIterator<I>> implements IPagination<T, I> {

    protected final MenuContents contents;
    protected final I iterator;

    protected final String id;
    protected final int itemsPerPage;

    protected int currentPage = 0;

    protected boolean initialized = false;

    private final boolean async;
    private volatile boolean switchingPage = false;

    private final T instance;

    AbstractPagination(MenuContents contents, String id, int itemsPerPage, I iterator, boolean async) {
        this.instance = this.self();

        this.contents = contents;
        this.id = id;
        this.itemsPerPage = itemsPerPage;
        this.iterator = iterator;
        this.async = async;
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
        if (this.switchingPage || page < 0 || page > this.lastPage()) return this.instance;

        this.switchingPage = true;

        BukkitThreadHelper.runCondition(this.async, this.contents.menuSession().getInstance().getJavaPlugin(), () -> {
            List<Supplier<MenuItem>> itemsOnPage = this.getItemsOnPage(page);
            if (itemsOnPage.isEmpty()) {
                this.switchingPage = false;
                return;
            }

            this.iterator.reset();

            this.currentPage = page;

            this.contents.cache().getPageSwitchUpdateItems().forEach((slot, item) -> {
                this.contents.set(slot, item.get());
            });

            Map<Integer, Supplier<MenuItem>> pageItems = new TreeMap<>();

            for (Supplier<MenuItem> itemSupplier : itemsOnPage) {
                int slot = this.iterator.next();

                if (itemSupplier == null) {
                    if (this.contents.isEmpty(slot)) continue;

                    this.contents.set(slot, DisplayItem.of(new ItemStack(Material.AIR)));
                    continue;
                }

                pageItems.put(slot, itemSupplier);
            }

            for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
                this.contents.set(entry.getKey(), entry.getValue().get());
            }

            this.switchingPage = false;
        });

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