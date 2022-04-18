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
public class TriteMenuObject {

    private Inventory inventory;
    private byte rows;
    private String displayName;
    private TriteMenuType menuType;
    private final TriteMenuItem[][] contents;

    private final Map<String, String> searchQueries = new HashMap<>();

    private boolean hasUpdatableItems = false;
    private boolean hasMenuOpened = false;

    public TriteMenuObject(byte rows, String displayName, TriteMenuType menuType) {
        this.contents = new TriteMenuItem[rows][9];

        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.menuType = menuType;
        this.displayName = displayName;
    }

    public TriteMenuItem getContent(TriteSlotPos triteSlotPos) {
        return this.contents[triteSlotPos.getRow()][triteSlotPos.getColumn()];
    }
}
