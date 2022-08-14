package nl.tritewolf.tritemenus.menu;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public final class MenuSession {

    private final Player player;

    private final MenuType menuType;
    private final Inventory inventory;
    private final int rows;
    private final int columns;
    private String title;

    private final MenuItem[][] contents;
    private volatile boolean hasUpdatableItems = false;

    private final MenuSessionCache cache;

    MenuSession(Player player, MenuType menuType, byte rows, String title) {
        this.player = player;
        this.menuType = menuType;

        if (menuType.type() == InventoryType.CHEST) {
            this.rows = rows;
            this.columns = 9;

            this.inventory = Bukkit.createInventory(null, rows * 9, title);
        } else {
            this.rows = Math.max(menuType.maxRows(), 1);
            this.columns = menuType.maxColumns() + menuType.otherSlots().size();

            this.inventory = Bukkit.createInventory(null, menuType.type(), title);
        }

        this.contents = new MenuItem[this.rows][this.columns];
        this.title = title;

        this.cache = new MenuSessionCache();
    }

    public void setTitle(@NotNull String title) {
        if (this.title.equals(title)) return;

        this.title = title;
        InventoryUtils.changeTitle(this.inventory, title);
    }

    public @Nullable MenuItem getContent(@NotNull SlotPos slotPos) {
        if (slotPos.getSlot() < 0 || slotPos.getSlot() > this.inventory.getSize()) return null;
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }

    public boolean fits(int slot) {
        return this.menuType.fitsInMenu(slot);
    }
}