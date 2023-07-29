package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

final class PaginationImpl extends AbstractPagination<Pagination, MenuIterator> implements Pagination {

    private final Map<Integer, List<Supplier<MenuItem>>> items = new HashMap<>();

    PaginationImpl(PaginationBuilderImpl builder, MenuIterator iterator) {
        super(builder, iterator);
    }

    @Override
    protected Pagination self() {
        return this;
    }

    @Override
    public @NotNull MenuIterator iterator() {
        return this.iterator;
    }

    @Override
    public int lastPage() {
        return Math.max(0, this.items.size() - 1);
    }

    @Override
    public synchronized @NotNull Pagination addItem(@NotNull Supplier<@NotNull MenuItem> itemSupplier) {
        int pageIndex = Math.max(this.items.size() - 1, 0);
        int index = this.items.getOrDefault(pageIndex, new ArrayList<>()).size();

        if ((this.initialized || this.contents.menuFrameData() != null) && index < this.itemsPerPage && pageIndex == this.currentPage) {
            this.iterator.setNext(itemSupplier.get());
        }

        if (index >= this.itemsPerPage) {
            pageIndex++;
        }

        this.items.computeIfAbsent(pageIndex, integer -> new ArrayList<>())
                .add(itemSupplier);

        if (this.currentPage + 1 <= this.lastPage()) {
            this.contents.cache().getPageSwitchUpdateItems().forEach((slot, item) -> {
                this.contents.set(slot, item.get());
            });
        }

        return this;
    }

    @ApiStatus.Internal
    @Override
    public @NotNull List<Supplier<MenuItem>> getItemsOnPage() {
        if (this.initialized || this.contents.menuFrameData() != null) return List.of();

        return this.getItemsOnPage(this.currentPage);
    }

    @Override
    protected List<Supplier<MenuItem>> getItemsOnPage(int page) {
        List<Supplier<MenuItem>> items = this.items.getOrDefault(page, new ArrayList<>());

        for (int i = items.size(); i < this.itemsPerPage; i++) {
            items.add(null);
        }

        return items;
    }
}