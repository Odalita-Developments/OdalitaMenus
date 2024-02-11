package nl.odalitadevelopments.menus.menu.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import nl.odalitadevelopments.menus.contents.MenuFrameData;
import nl.odalitadevelopments.menus.contents.MenuTask;
import nl.odalitadevelopments.menus.contents.action.MenuCloseResult;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
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

    private final Map<String, IPagination<?, ?>> paginationMap = Maps.newConcurrentMap();
    private final Map<String, Scrollable> scrollableMap = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<MenuItem>> refreshableItems = Maps.newConcurrentMap();
    private final Map<Integer, Supplier<PageUpdatableItem>> pageSwitchUpdateItems = Maps.newConcurrentMap();

    private final Map<String, MenuIterator> iterators = Maps.newConcurrentMap();

    private final List<Integer> placeableItems = Lists.newCopyOnWriteArrayList();
    private boolean allowPlaceableItemShiftClick = true;
    private boolean allowPlaceableItemDrag = true;
    private PlaceableItemsCloseAction placeableItemsCloseAction = null;
    private PlaceableItemClickAction placeableItemClickAction = null;
    private PlaceableItemShiftClickAction placeableItemShiftClickAction = null;
    private PlaceableItemDragAction placeableItemDragAction = null;

    private Consumer<InventoryClickEvent> playerInventoryClickAction = null;
    private Supplier<MenuCloseResult> closeActionBefore = null;
    private Supplier<MenuCloseResult> closeActionAfter = null;

    private PlayerInventoryItemMetaChanger itemMetaChanger = null;

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

    public Map<String, Object> getCache() {
        if (!this.menuSession.getGlobalCacheKey().isEmpty() && !this.menuSession.getGlobalCacheKey().isBlank()) {
            return this.menuSession.getInstance().getGlobalSessionCache().getOrCreateCache(this.menuSession);
        }

        return this.cache;
    }
}