package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
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

    private MenuType menuType;
    private Inventory inventory;
    private byte rows;
    private byte columns;
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

    public MenuObject(Player player, MenuType menuType, byte rows, String displayName) {
        this.player = player;
        this.menuType = menuType;

        this.rows = rows;
        this.columns = 9;

        if (menuType.type() == InventoryType.CHEST) {
            this.contents = new MenuItem[rows][9];
            this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        } else {
            this.rows = (byte) Math.max(menuType.maxRows(), 1);
            this.columns = (byte) (menuType.maxColumns() + menuType.otherSlots().size());

            this.contents = new MenuItem[this.rows][this.columns];
            this.inventory = Bukkit.createInventory(null, menuType.type(), displayName);
        }

        this.displayName = displayName;
    }

    public @Nullable MenuItem getContent(@NotNull SlotPos slotPos) {
        if (slotPos.getSlot() < 0 || slotPos.getSlot() > this.inventory.getSize()) return null;
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }

    public boolean fits(int slot) {
        return this.menuType.fitsInMenu(slot);
    }
}