package nl.tritewolf.tritemenus.contents.pagination;

import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.MenuItem;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Pagination {

    private final InventoryContents contents;
    private final int itemsPerPage;
    private final List<Supplier<MenuItem>> items;

    public List<Supplier<MenuItem>> getItemsOnPage(int page) {
        int from = page * this.itemsPerPage;
        int to = (page + 1) * this.itemsPerPage;

        if (from < 0 || to > this.items.size()) {
            return this.items.subList(
                    0,
                    this.items.size() - 1
            );
        }

        return this.items.subList(
                page * this.itemsPerPage,
                (page + 1) * this.itemsPerPage
        );
    }

    public boolean isFirst(int page) {
        return page == 0;
    }

    public boolean isLast(int page) {
        int pageCount = (int) Math.ceil((double) this.items.size() / this.itemsPerPage);
        return page >= pageCount - 1;
    }
}