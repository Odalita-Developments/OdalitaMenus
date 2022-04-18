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
    private final Map<TriteSlotPos, TriteMenuItem> contents = new HashMap<>();

    private final Map<String, String> searchQueries = new HashMap<>();

    private boolean hasUpdatableItems = false;
    private boolean hasMenuOpened = false;

    public TriteMenuObject(byte rows, String displayName, TriteMenuType menuType) {
        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.menuType = menuType;
        this.displayName = displayName;
    }

    public TriteMenuItem getContent(TriteSlotPos triteSlotPos) {
        Map.Entry<TriteSlotPos, TriteMenuItem> menuItem = contents.entrySet().stream().filter(entry -> entry.getKey().equals(triteSlotPos)).findFirst().orElse(null);
        if (menuItem != null) {
            return menuItem.getValue();
        }

        return null;
    }
}
