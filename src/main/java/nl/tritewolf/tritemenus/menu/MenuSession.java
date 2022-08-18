package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private boolean opened = false;
    @Getter(AccessLevel.PACKAGE)
    private final List<Runnable> actionsAfterOpening = new ArrayList<>();

    MenuSession(Player player, MenuType menuType, byte rows, Inventory inventory, String title) {
        this.player = player;
        this.menuType = menuType;

        if (menuType.type() == InventoryType.CHEST) {
            this.rows = rows;
            this.columns = 9;
        } else {
            this.rows = Math.max(menuType.maxRows(), 1);
            this.columns = menuType.maxColumns() + menuType.otherSlots().size();
        }

        this.inventory = inventory;
        this.contents = new MenuItem[this.rows][this.columns];
        this.title = title;

        this.cache = new MenuSessionCache();
    }

    public synchronized void setTitle(@NotNull String title) {
        if (this.title.equals(title)) return;

        this.title = title;

        if (!this.opened) {
            this.actionsAfterOpening.add(() -> InventoryUtils.changeTitle(this.inventory, title));
        } else {
            InventoryUtils.changeTitle(this.inventory, title);
        }
    }

    public @Nullable MenuItem getContent(@NotNull SlotPos slotPos) {
        if (slotPos.getSlot() < 0 || slotPos.getSlot() > this.inventory.getSize()) return null;
        return this.contents[slotPos.getRow()][slotPos.getColumn()];
    }

    public boolean fits(int slot) {
        return this.menuType.fitsInMenu(slot);
    }
}