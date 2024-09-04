package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.AbstractMenuIterator;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
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

    protected final AbstractMenuSession<?, ?, ?> menuSession;
    protected final I iterator;

    protected final String id;
    protected final int itemsPerPage;

    protected int currentPage = 0;

    protected boolean initialized = false;

    protected final boolean async;
    private volatile boolean switchingPage = false;

    private final T instance;

    AbstractPagination(PaginationBuilderImpl builder, I iterator) {
        this.instance = this.self();

        this.menuSession = builder.menuSession;
        this.id = builder.id;
        this.itemsPerPage = builder.itemsPerPage;
        this.async = builder.async;

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
        if (this.switchingPage || page < 0 || page > this.lastPage()) return this.instance;

        this.switchingPage = true;

        BukkitThreadHelper.runCondition(this.async, this.menuSession.instance().getJavaPlugin(), () -> {
            List<Supplier<MenuItem>> itemsOnPage = this.getItemsOnPage(page);
            if (itemsOnPage.isEmpty()) {
                this.switchingPage = false;
                return;
            }

            this.iterator.reset();

            this.currentPage = page;

            this.menuSession.cache().getPageSwitchUpdateItems().forEach((slot, item) -> {
                this.menuSession.menuContents().set(slot, item.get());
            });

            Map<Integer, Supplier<MenuItem>> pageItems = new TreeMap<>();

            for (Supplier<MenuItem> itemSupplier : itemsOnPage) {
                int slot = this.iterator.next();

                if (!this.menuSession.menuContents().isEmpty(slot)) {
                    this.menuSession.menuContents().set(slot, DisplayItem.of(new ItemStack(Material.AIR)));
                }

                if (itemSupplier != null) {
                    pageItems.put(slot, itemSupplier);
                }
            }

            for (Map.Entry<Integer, Supplier<MenuItem>> entry : pageItems.entrySet()) {
                this.menuSession.menuContents().set(entry.getKey(), entry.getValue().get());
            }

            this.switchingPage = false;
        });

        return this.instance;
    }

    @Override
    public boolean isAsync() {
        return this.async;
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