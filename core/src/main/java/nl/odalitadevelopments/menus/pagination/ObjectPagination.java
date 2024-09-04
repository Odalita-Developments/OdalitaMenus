package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public sealed interface ObjectPagination<T> extends IPagination<ObjectPagination<T>, MenuObjectIterator<T>> permits ObjectPaginationImpl {

    @NotNull
    ObjectPagination<T> addItem(@NotNull T value);

    @NotNull
    ObjectPagination<T> emptyFilteredItemsAction(@NotNull Consumer<IMenuContents> consumer);

    @NotNull
    ObjectPagination<T> emptyFilteredItemsAction(@NotNull Runnable runnable);

    @NotNull
    ObjectPagination<T> filter(@NotNull String id, @NotNull Predicate<T> predicate);

    @NotNull
    ObjectPagination<T> sorter(int priority, @NotNull Comparator<@NotNull T> comparator);

    @NotNull
    ObjectPagination<T> removeFilter(@NotNull String id);

    @NotNull
    ObjectPagination<T> removeSorter(int priority);

    boolean apply();

    void createBatch();

    void endBatch();
}