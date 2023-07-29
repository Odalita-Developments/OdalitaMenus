package nl.odalitadevelopments.menus.pagination;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class PaginationBuilderImpl implements PaginationBuilder {

    final MenuContents contents;

    final String id;
    final int itemsPerPage;

    boolean async = false;

    @Override
    public @NotNull PaginationBuilder asyncPageSwitching(boolean async) {
        this.async = async;
        return this;
    }

    @Override
    public @NotNull ItemPaginationBuilder iterator(@NotNull MenuIterator iterator) {
        return new ItemPaginationBuilderImpl(this, iterator);
    }

    @Override
    public @NotNull ItemPaginationBuilder iterator(@NotNull Supplier<@NotNull MenuIterator> iteratorSupplier) {
        return this.iterator(iteratorSupplier.get());
    }

    @Override
    public @NotNull ItemPaginationBuilder iterator(@NotNull Function<@NotNull MenuContents, @NotNull MenuIterator> iteratorFunction) {
        return this.iterator(iteratorFunction.apply(this.contents));
    }

    @Override
    public @NotNull <T> ObjectPaginationBuilder<T> objectIterator(@NotNull MenuObjectIterator<T> iterator) {
        return new ObjectPaginationBuilderImpl<>(this, iterator);
    }

    @Override
    public @NotNull <T> ObjectPaginationBuilder<T> objectIterator(@NotNull Supplier<@NotNull MenuObjectIterator<T>> menuObjectIteratorSupplier) {
        return this.objectIterator(menuObjectIteratorSupplier.get());
    }

    @Override
    public @NotNull <T> ObjectPaginationBuilder<T> objectIterator(@NotNull Function<@NotNull MenuContents, @NotNull MenuObjectIterator<T>> menuContentsMenuObjectIteratorFunction) {
        return this.objectIterator(menuContentsMenuObjectIteratorFunction.apply(this.contents));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static final class ItemPaginationBuilderImpl implements ItemPaginationBuilder {

        private final PaginationBuilderImpl builder;
        private final MenuIterator iterator;

        private List<Supplier<MenuItem>> items = new ArrayList<>();

        @Override
        public @NotNull ItemPaginationBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items) {
            this.items = items;
            return this;
        }

        @Override
        public @NotNull Pagination create() {
            if (this.iterator == null) {
                throw new IllegalStateException("You have to set an iterator before creating the pagination");
            }

            PaginationImpl pagination = new PaginationImpl(this.builder, this.iterator);
            this.items.forEach(pagination::addItem);

            this.builder.contents.menuSession().getCache().getPaginationMap().put(this.builder.id, pagination);

            return pagination;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static final class ObjectPaginationBuilderImpl<T> implements ObjectPaginationBuilder<T> {

        private final PaginationBuilderImpl builder;
        private final MenuObjectIterator<T> iterator;

        private Collection<T> objects = new ArrayList<>();

        @Override
        public @NotNull ObjectPaginationBuilder<T> objects(@NotNull Collection<@NotNull T> objects) {
            this.objects = objects;
            return this;
        }

        @Override
        public @NotNull ObjectPagination<T> create() {
            if (this.iterator == null) {
                throw new IllegalStateException("You have to set an iterator before creating the pagination");
            }

            ObjectPaginationImpl<T> pagination = new ObjectPaginationImpl<>(this.builder, this.iterator);
            this.iterator.pagination(pagination);

            this.objects.forEach(pagination::addItem);

            this.builder.contents.menuSession().getCache().getPaginationMap().put(this.builder.id, pagination);

            return pagination;
        }
    }
}