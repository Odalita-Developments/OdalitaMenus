package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface PaginationBuilder permits PaginationBuilderImpl {

    static @NotNull PaginationBuilder builder(@NotNull AbstractMenuSession<?, ?, ?> menuSession, @NotNull String id, int itemsPerPage) {
        return new PaginationBuilderImpl(menuSession, id, itemsPerPage);
    }

    @NotNull
    PaginationBuilder asyncPageSwitching(boolean async);

    @NotNull
    ItemPaginationBuilder iterator(@NotNull MenuIterator iterator);

    @NotNull
    ItemPaginationBuilder iterator(@NotNull Supplier<@NotNull MenuIterator> iteratorSupplier);

    @NotNull
    ItemPaginationBuilder iterator(@NotNull Function<@NotNull IMenuContents, @NotNull MenuIterator> iteratorFunction);

    <T> @NotNull ObjectPaginationBuilder<T> objectIterator(@NotNull MenuObjectIterator<T> iterator);

    <T> @NotNull ObjectPaginationBuilder<T> objectIterator(@NotNull Supplier<@NotNull MenuObjectIterator<T>> iteratorSupplier);

    <T> @NotNull ObjectPaginationBuilder<T> objectIterator(@NotNull Function<@NotNull IMenuContents, @NotNull MenuObjectIterator<T>> iteratorFunction);

    sealed interface ItemPaginationBuilder permits PaginationBuilderImpl.ItemPaginationBuilderImpl {

        @NotNull
        ItemPaginationBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

        @NotNull
        Pagination create();
    }

    sealed interface ObjectPaginationBuilder<T> permits PaginationBuilderImpl.ObjectPaginationBuilderImpl {

        @NotNull
        ObjectPaginationBuilder<T> objects(@NotNull Collection<@NotNull T> objects);

        @NotNull
        ObjectPagination<T> create();
    }
}