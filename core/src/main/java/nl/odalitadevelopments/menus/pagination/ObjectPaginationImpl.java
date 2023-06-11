package nl.odalitadevelopments.menus.pagination;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

final class ObjectPaginationImpl<T> extends AbstractPagination<ObjectPagination<T>, MenuObjectIterator<T>> implements ObjectPagination<T> {

    ObjectPaginationImpl(MenuContents contents, String id, int itemsPerPage, MenuObjectIterator<T> iterator) {
        super(contents, id, itemsPerPage, iterator);
    }

    @Override
    protected ObjectPagination<T> self() {
        return this;
    }

    @Override
    public @NotNull MenuObjectIterator<T> iterator() {
        return this.iterator;
    }

    @Override
    public int lastPage() {
        return (int) (Math.ceil((double) this.iterator.getObjects().size() / (double) this.itemsPerPage) - 1);
    }

    @Override
    public @NotNull ObjectPagination<T> addItem(@NotNull T value) {
        this.iterator.add(value);
        return this;
    }

    @Override
    public void sort(@NotNull Comparator<@NotNull T> comparator) {
        // Sort items
        this.iterator.sort(comparator);
    }

    @ApiStatus.Internal
    @Override
    public @NotNull List<Supplier<MenuItem>> getItemsOnPage() {
        if (this.initialized || this.contents.menuFrameData() != null) return List.of();

        return this.getItemsOnPage(this.currentPage);
    }

    @Override
    protected List<Supplier<MenuItem>> getItemsOnPage(int page) {
        List<Supplier<MenuItem>> items = new ArrayList<>();

        List<T> objects = this.iterator.getObjects();
        List<T> pageList = objects.subList(page * this.itemsPerPage, Math.min(objects.size(), (page + 1) * this.itemsPerPage));
        System.out.println(pageList);

        for (T value : pageList) {
            MenuItem menuItem = this.iterator.createMenuItem(value);
            if (menuItem == null) continue;

            items.add(() -> menuItem);
        }

        for (int i = items.size(); i < this.itemsPerPage; i++) {
            items.add(null);
        }

        return items;
    }
}