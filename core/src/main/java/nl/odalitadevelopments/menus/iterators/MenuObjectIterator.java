package nl.odalitadevelopments.menus.iterators;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.pagination.ObjectPagination;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MenuObjectIterator<T> extends AbstractMenuIterator<MenuObjectIterator<T>> {

    private final List<T> allObjects;
    private final Function<T, MenuItem> menuItemCreatorFunction;

    private boolean batch = false;
    private final Map<String, Predicate<T>> filters = new HashMap<>();
    private final Map<Integer, Comparator<T>> sorters = new TreeMap<>();

    private List<T> filteredObjects;

    private ObjectPagination<T> pagination = null;

    public MenuObjectIterator(MenuContents contents, MenuIteratorType type, int startRow, int startColumn, Function<T, MenuItem> menuItemCreatorFunction) {
        super(contents, type, startRow, startColumn);

        this.allObjects = new ArrayList<>();
        this.menuItemCreatorFunction = menuItemCreatorFunction;
    }

    @Override
    protected @NotNull MenuObjectIterator<T> self() {
        return this;
    }

    public @NotNull MenuObjectIterator<T> filter(@NotNull String id, @NotNull Predicate<@NotNull T> predicate) {
        this.filters.put(id, predicate);
        return this;
    }

    public @NotNull MenuObjectIterator<T> sorter(int priority, @NotNull Comparator<@NotNull T> comparator) {
        this.sorters.put(priority, comparator);
        return this;
    }

    public @NotNull MenuObjectIterator<T> removeFilter(@NotNull String id) {
        this.filters.remove(id);
        return this;
    }

    public @NotNull MenuObjectIterator<T> removeSorter(int priority) {
        this.sorters.remove(priority);
        return this;
    }

    public void createBatch() {
        this.batch = true;
    }

    public void endBatch() {
        this.batch = false;
        this.apply();
    }

    public void add(@NotNull T value) {
        this.allObjects.add(value);

        if (!this.batch) {
            this.apply();
        }
    }

    public void apply() {
        if (this.pagination != null && !this.contents.menuSession().isInitialized()) return;

        super.reset();

        this.filteredObjects = new ArrayList<>(this.allObjects);
        for (Predicate<T> filter : this.filters.values()) {
            this.filteredObjects.removeIf(filter.negate());
        }

        Comparator<T> comparator = null;
        for (Comparator<T> sorter : this.sorters.values()) {
            if (comparator == null) {
                comparator = sorter;
                continue;
            }

            comparator = comparator.thenComparing(sorter);
        }

        if (comparator != null) {
            this.filteredObjects.sort(comparator);
        }

        if (this.pagination == null) {
            for (T value : this.allObjects) {
                if (this.hasNext()) {
                    MenuItem item = this.menuItemCreatorFunction.apply(value);
                    if (item == null) continue;

                    int slot = this.next();
                    this.contents.set(slot, item);
                }
            }
        } else {
            // If we use it as a pagination, we need to reopen the pagination on the current page
            this.pagination.open(this.pagination.currentPage());
        }
    }

    @ApiStatus.Internal
    public @NotNull List<T> getObjects() {
        return (this.filteredObjects == null) ? List.of() : List.copyOf(this.filteredObjects);
    }

    @ApiStatus.Internal
    public @UnknownNullability MenuItem createMenuItem(@NotNull T value) {
        return this.menuItemCreatorFunction.apply(value);
    }

    @ApiStatus.Internal
    public void pagination(@NotNull ObjectPagination<T> pagination) {
        if (this.pagination == null) {
            this.pagination = pagination;
        }
    }
}