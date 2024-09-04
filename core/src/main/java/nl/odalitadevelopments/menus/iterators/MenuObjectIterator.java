package nl.odalitadevelopments.menus.iterators;

import com.google.common.collect.Lists;
import nl.odalitadevelopments.menus.contents.frame.MenuFrameData;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.pagination.ObjectPagination;
import nl.odalitadevelopments.menus.utils.BukkitThreadHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MenuObjectIterator<T> extends AbstractMenuIterator<MenuObjectIterator<T>> {

    private final List<T> allObjects;
    private final Function<T, MenuItem> menuItemCreatorFunction;

    private boolean batch = false;
    private final Map<String, Predicate<T>> filters = new HashMap<>();
    private final Map<Integer, Comparator<T>> sorters = new TreeMap<>();

    private volatile List<T> filteredObjects;
    private Consumer<IMenuContents> emptyFilteredItemsConsumer = null;

    private ObjectPagination<T> pagination = null;

    private volatile boolean isApplying = false;

    public MenuObjectIterator(AbstractMenuSession<?, ?, ?> menuSession, MenuFrameData frameData, MenuIteratorType type, int startRow, int startColumn, Function<T, MenuItem> menuItemCreatorFunction) {
        super(menuSession, frameData, type, startRow, startColumn);

        this.allObjects = Lists.newCopyOnWriteArrayList();
        this.menuItemCreatorFunction = menuItemCreatorFunction;
    }

    @Override
    protected @NotNull MenuObjectIterator<T> self() {
        return this;
    }

    public @NotNull MenuObjectIterator<T> emptyFilteredItemsAction(@NotNull Consumer<IMenuContents> consumer) {
        this.emptyFilteredItemsConsumer = consumer;
        return this;
    }

    public @NotNull MenuObjectIterator<T> emptyFilteredItemsAction(@NotNull Runnable runnable) {
        this.emptyFilteredItemsConsumer = ($) -> runnable.run();
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

    public boolean apply() {
        if (this.isApplying) return false;

        super.reset();

        BukkitThreadHelper.runCondition(this.pagination != null && this.pagination.isAsync(), this.menuSession.instance().getJavaPlugin(), () -> {
            this.isApplying = true;

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
                    if (super.hasNext()) {
                        MenuItem item = this.menuItemCreatorFunction.apply(value);
                        if (item == null) continue;

                        int slot = super.next();
                        this.contents.set(slot, item);
                    }
                }
            } else {
                // If we use it as a pagination, we need to reopen the pagination to apply the changes.
                // If there is a filter present open the on the first page, to make sure the changes are visible
                // Else open it on the current page.
                int page = !this.filters.isEmpty() ? 0 : this.pagination.currentPage();
                this.pagination.open(page);
            }

            if (this.filteredObjects.isEmpty() && this.emptyFilteredItemsConsumer != null) {
                this.emptyFilteredItemsConsumer.accept(this.contents);
            }

            this.isApplying = false;
        });

        return true;
    }

    @ApiStatus.Internal
    public @NotNull List<T> getObjects() {
        return (this.filteredObjects == null) ? List.of() : List.copyOf(this.filteredObjects);
    }

    @ApiStatus.Internal
    public MenuItem createMenuItem(@NotNull T value) {
        return this.menuItemCreatorFunction.apply(value);
    }

    @ApiStatus.Internal
    public void pagination(@NotNull ObjectPagination<T> pagination) {
        if (this.pagination == null) {
            this.pagination = pagination;
        }
    }
}