package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import org.jetbrains.annotations.NotNull;

public sealed interface ObjectPagination<T> extends IPagination<ObjectPagination<T>, MenuObjectIterator<T>> permits ObjectPaginationImpl {

    @NotNull ObjectPagination<T> addItem(@NotNull T value);
}