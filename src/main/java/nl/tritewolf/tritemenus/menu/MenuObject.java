package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class MenuObject {

    private Inventory inventory;
    private byte rows;
    private String displayName;
    private final MenuItem[][] contents;
    private List<Integer> placeableItems = new ArrayList<>();

    private final Map<String, String> searchQueries = new HashMap<>();

    private boolean hasUpdatableItems = false;

    public MenuObject(byte rows, String displayName) {
        this.contents = new MenuItem[rows][9];

        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.displayName = displayName;
    }

    public MenuItem getContent(SlotPos slotPos) {
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }
}