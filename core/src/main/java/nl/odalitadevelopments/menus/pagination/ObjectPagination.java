package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public sealed interface ObjectPagination<T> extends IPagination<ObjectPagination<T>, MenuObjectIterator<T>> permits ObjectPaginationImpl {

    @NotNull ObjectPagination<T> addItem(@NotNull T value);

    void sort(@NotNull Comparator<@NotNull T> comparator);
}