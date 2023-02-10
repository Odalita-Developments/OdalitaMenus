package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PaginationBuilder {

    static @NotNull PaginationBuilder builder(@NotNull InventoryContents contents, @NotNull String id, int itemsPerPage) {
        return new PaginationBuilderImpl(contents, id, itemsPerPage);
    }

    @NotNull PaginationBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

    @NotNull PaginationBuilder iterator(@NotNull MenuIterator iterator);

    @NotNull PaginationBuilder iterator(@NotNull Supplier<@NotNull MenuIterator> iteratorSupplier);

    @NotNull PaginationBuilder iterator(@NotNull Function<@NotNull InventoryContents, @NotNull MenuIterator> iteratorFunction);

    @NotNull Pagination create();
}