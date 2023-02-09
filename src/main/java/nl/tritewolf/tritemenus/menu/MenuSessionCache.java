package nl.tritewolf.tritemenus.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.MenuFrameData;
import nl.tritewolf.tritemenus.contents.MenuTask;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
public final class MenuSessionCache {

    private final MenuSession menuSession;

    private final Map<String, Pagination> paginationMap = Maps.newConcurrentMap();
    private final Map<String, Scrollable> scrollableMap = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<MenuItem>> refreshableItems = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = Maps.newConcurrentMap();

    private final Map<String, MenuIterator> iterators = Maps.newConcurrentMap();

    private final Map<String, String> searchQueries = Maps.newConcurrentMap();

    private final List<Integer> placeableItems = Lists.newCopyOnWriteArrayList();
    private PlaceableItemsCloseAction placeableItemsCloseAction = null;
    private PlaceableItemAction placeableItemAction = null;
    private Consumer<InventoryClickEvent> playerInventoryClickAction = null;

    private final Map<String, MenuFrameData> frames = Maps.newConcurrentMap();
    private final Collection<Integer> frameOverlaySlots = Sets.newConcurrentHashSet();
    private String loadedFrameId = null;

    private final Map<String, MenuTask> tasks = Maps.newConcurrentMap();

    private final Map<String, Object> cache = Maps.newConcurrentMap();

    public MenuSessionCache(MenuSession menuSession) {
        this.menuSession = menuSession;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T cache(@NotNull String key, T def) {
        return (T) this.getCache().getOrDefault(key, def);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T cache(@NotNull String key) {
        return (T) this.getCache().get(key);
    }

    public synchronized void setCache(@NotNull String key, @NotNull Object value) {
        this.getCache().put(key, value);
    }

    public synchronized void pruneCache(@NotNull String key) {
        this.getCache().remove(key);
    }

    Map<String, Object> getCache() {
        if (!this.menuSession.getGlobalCacheKey().isEmpty() && !this.menuSession.getGlobalCacheKey().isBlank()) {
            return TriteMenus.getInstance().getSessionCache().getOrCreateCache(this.menuSession);
        }

        return this.cache;
    }
}