package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.buttons.NextItem;
import nl.tritewolf.tritemenus.items.buttons.PreviousItem;
import nl.tritewolf.tritemenus.pagination.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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
    private final Map<String, String> searchQueries = new HashMap<>();

    private final Map<Integer, Supplier<MenuItem>> pageSwitchUpdateItems = new HashMap<>();
    private List<Integer> placeableItems = new ArrayList<>();
    private PlaceableItemsCloseAction placeableItemsCloseAction;

    private int slot;

    private volatile boolean hasUpdatableItems = false;

    public MenuObject(Player player, byte rows, String displayName) {
        this.player = player;
        this.contents = new MenuItem[rows][9];

        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.displayName = displayName;
    }

    public MenuItem getContent(SlotPos slotPos) {
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }
}