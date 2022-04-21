package nl.tritewolf.tritemenus.contents.pagination;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;

import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
@RequiredArgsConstructor
public class Pagination {

    private final InventoryContents contents;
    private final int itemsPerPage;
    private final MenuIterator iterator;
    private final List<Supplier<MenuItem>> items;

    @Setter
    private int currentPage = 0;

    public boolean isOnPage(int index) {
        return index >= this.currentPage * this.itemsPerPage && index < (this.currentPage + 1) * this.itemsPerPage;
    }

    public List<Supplier<MenuItem>> getItemsOnPage() {
        int from = this.currentPage * this.itemsPerPage;
        int to = (this.currentPage + 1) * this.itemsPerPage;

        if (from < 0 || to > this.items.size()) {
            return this.items.subList(
                    0,
                    this.items.size() - 1
            );
        }

        return this.items.subList(
                this.currentPage * this.itemsPerPage,
                (this.currentPage + 1) * this.itemsPerPage
        );
    }

    public boolean isFirst() {
        return this.currentPage == 0;
    }

    public boolean isLast() {
        int pageCount = (int) Math.ceil((double) this.items.size() / this.itemsPerPage);
        return this.currentPage >= pageCount - 1;
    }

    public Pagination addItem(Supplier<MenuItem> menuItemSupplier) {
        if (this.isOnPage(this.items.size())) {
            System.out.println("SET ITEM ASYNC");
            this.iterator.setAsync(menuItemSupplier.get()).next();
        }

        this.items.add(menuItemSupplier);
        return this;
    }
}