package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    private boolean hasUpdatableItems = false;
    private boolean hasMenuOpened = false;

    public TriteMenuObject(byte rows, String displayName, TriteMenuType menuType) {
        this.inventory = Bukkit.createInventory(null, rows * 9, displayName);
        this.rows = rows;
        this.menuType = menuType;
        this.displayName = displayName;
    }
}
