package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
@Setter
public final class MenuObject {

    private final Player player;

    private Inventory inventory;
    private byte rows;
    private String displayName;

    private final MenuItem[][] contents;
    private final Map<String, Pagination> paginationMap = new ConcurrentHashMap<>();
    private final Map<String, Scrollable> scrollableMap = new ConcurrentHashMap<>();
    private final Map<String, MenuIterator> iterators = new HashMap<>();
    private final Map<String, String> searchQueries = new HashMap<>();

    private final Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = new HashMap<>();
    private final List<Integer> placeableItems = new ArrayList<>();
    private PlaceableItemsCloseAction placeableItemsCloseAction;

    private int slot;

    private volatile boolean hasUpdatableItems = false;

    public MenuObject(Player player, byte rows, String displayName) {
        this.player = player;
        this.contents = new MenuItem[rows][this.getColumns()];

        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.displayName = displayName;
    }

    public @Nullable MenuItem getContent(@NotNull SlotPos slotPos) {
        if (slotPos.getSlot() < 0 || slotPos.getSlot() > this.inventory.getSize()) return null;
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }

    public int getColumns() {
        return 9;
    }

    public int getSize() {
        return this.inventory.getSize();
    }
}