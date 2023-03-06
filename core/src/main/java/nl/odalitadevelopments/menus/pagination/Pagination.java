package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public sealed interface Pagination permits PaginationImpl {

    @NotNull String id();

    int itemsPerPage();

    @NotNull MenuIterator iterator();

    int currentPage();

    int firstPage();

    int lastPage();

    boolean isFirstPage();

    boolean isLastPage();

    @NotNull Pagination addItem(@NotNull Supplier<@NotNull MenuItem> item);

    @NotNull Pagination nextPage();

    @NotNull Pagination previousPage();

    @NotNull Pagination open(int page);

    @ApiStatus.Internal
    void setPage(int page);

    @ApiStatus.Internal
    void setInitialized();

    @ApiStatus.Internal
    @NotNull List<Supplier<MenuItem>> getItemsOnPage();
}