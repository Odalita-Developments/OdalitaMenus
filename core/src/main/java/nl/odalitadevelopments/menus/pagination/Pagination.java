package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public sealed interface Pagination extends IPagination<Pagination, MenuIterator> permits PaginationImpl {

    @NotNull Pagination addItem(@NotNull Supplier<@NotNull MenuItem> item);
}