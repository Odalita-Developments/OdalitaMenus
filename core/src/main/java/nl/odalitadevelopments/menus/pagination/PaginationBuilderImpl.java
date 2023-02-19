package nl.odalitadevelopments.menus.pagination;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class PaginationBuilderImpl implements PaginationBuilder {

    private final MenuContents contents;

    private final String id;
    private final int itemsPerPage;

    private List<Supplier<MenuItem>> items = new ArrayList<>();
    private MenuIterator iterator;

    @Override
    public @NotNull PaginationBuilder items(@NotNull List<@NotNull Supplier<@NotNull MenuItem>> items) {
        this.items = items;
        return this;
    }

    @Override
    public @NotNull PaginationBuilder iterator(@NotNull MenuIterator iterator) {
        this.iterator = iterator;
        return this;
    }

    @Override
    public @NotNull PaginationBuilder iterator(@NotNull Supplier<@NotNull MenuIterator> iteratorSupplier) {
        return this.iterator(iteratorSupplier.get());
    }

    @Override
    public @NotNull PaginationBuilder iterator(@NotNull Function<@NotNull MenuContents, @NotNull MenuIterator> iteratorFunction) {
        return this.iterator(iteratorFunction.apply(this.contents));
    }

    @Override
    public @NotNull Pagination create() {
        if (this.iterator == null) {
            throw new IllegalStateException("You have to set an iterator before creating the pagination");
        }

        PaginationImpl pagination = new PaginationImpl(this.contents, this.id, this.itemsPerPage, this.iterator);
        this.items.forEach(pagination::addItem);

        this.contents.menuSession().getCache().getPaginationMap().put(this.id, pagination);

        return pagination;
    }
}