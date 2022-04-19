package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class TriteMenuObject {

    private Inventory inventory;
    private byte rows;
    private String displayName;
    private final TriteMenuItem[][] contents;

    private final Map<String, String> searchQueries = new HashMap<>();

    private boolean hasUpdatableItems = false;

    public TriteMenuObject(byte rows, String displayName) {
        this.contents = new TriteMenuItem[rows][9];

        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.displayName = displayName;
    }

    public TriteMenuItem getContent(TriteSlotPos triteSlotPos) {
        return this.contents[triteSlotPos.getRow()][triteSlotPos.getColumn()];
    }
}