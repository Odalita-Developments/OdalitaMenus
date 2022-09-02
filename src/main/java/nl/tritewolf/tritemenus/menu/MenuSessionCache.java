package nl.tritewolf.tritemenus.menu;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Getter
@Setter
public final class MenuSessionCache {

    private final Map<String, Pagination> paginationMap = Maps.newConcurrentMap();
    private final Map<String, Scrollable> scrollableMap = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<MenuItem>> refreshableItems = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = Maps.newConcurrentMap();

    private final Map<String, MenuIterator> iterators = Maps.newConcurrentMap();

    private final Map<String, String> searchQueries = Maps.newConcurrentMap();

    private final Set<Integer> placeableItems = Sets.newConcurrentHashSet();
    private PlaceableItemsCloseAction placeableItemsCloseAction = null;
    private PlaceableItemAction placeableItemAction = null;

    private final Map<String, MenuTask> tasks = Maps.newConcurrentMap();

    private final Map<String, Object> cache = Maps.newConcurrentMap();

    MenuSessionCache() {
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T cache(@NotNull String key, T def) {
        return (T) this.cache.getOrDefault(key, def);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T cache(@NotNull String key) {
        return (T) this.cache.get(key);
    }

    public synchronized void setCache(@NotNull String key, @NotNull Object value) {
        this.cache.put(key, value);
    }

    public synchronized void pruneCache(@NotNull String key) {
        this.cache.remove(key);
    }
}