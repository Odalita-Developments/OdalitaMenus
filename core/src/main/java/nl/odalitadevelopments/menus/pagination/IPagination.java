package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.AbstractMenuIterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public sealed interface IPagination<T extends IPagination<T, I>, I extends AbstractMenuIterator<I>> permits Pagination, ObjectPagination, AbstractPagination {

    @NotNull
    String id();

    @NotNull
    I iterator();

    int itemsPerPage();

    int currentPage();

    int firstPage();

    int lastPage();

    boolean isFirstPage();

    boolean isLastPage();

    @NotNull
    T nextPage();

    @NotNull
    T previousPage();

    @NotNull
    T open(int page);

    boolean isAsync();

    @ApiStatus.Internal
    void setPage(int page);

    @ApiStatus.Internal
    void setInitialized();

    @ApiStatus.Internal
    @NotNull
    List<Supplier<MenuItem>> getItemsOnPage();
}