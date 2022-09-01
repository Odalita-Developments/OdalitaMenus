package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
@Setter
public final class MenuSessionCache {

    private final Map<String, Pagination> paginationMap = new ConcurrentHashMap<>();
    private final Map<String, Scrollable> scrollableMap = new ConcurrentHashMap<>();
    private final Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = new HashMap<>();

    private final Map<String, MenuIterator> iterators = new HashMap<>();

    private final Map<String, String> searchQueries = new HashMap<>();

    private final List<Integer> placeableItems = new ArrayList<>();
    private PlaceableItemsCloseAction placeableItemsCloseAction = null;
    private PlaceableItemAction placeableItemAction = null;

    private final Map<String, MenuTask> tasks = new ConcurrentHashMap<>();

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

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