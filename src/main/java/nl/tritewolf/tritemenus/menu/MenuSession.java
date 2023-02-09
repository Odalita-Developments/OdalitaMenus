package nl.tritewolf.tritemenus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class MenuSession {

    private final Player player;

    private final MenuType menuType;
    private final Inventory inventory;
    private final int rows;
    private final int columns;
    private String title;
    private final InventoryContents inventoryContents;

    private volatile MenuItem[][] contents;
    private volatile boolean hasUpdatableItems = false;

    private String globalCacheKey;
    private final MenuSessionCache cache;

    @Setter(AccessLevel.PACKAGE)
    private boolean opened = false;
    @Getter(AccessLevel.PACKAGE)
    private final List<Runnable> actionsAfterOpening = new ArrayList<>();

    MenuSession(Player player, MenuType menuType, byte rows, Inventory inventory, String title, String globalCacheKey) {
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

        this.globalCacheKey = globalCacheKey;
        this.cache = new MenuSessionCache(this);

        this.inventoryContents = InventoryContents.create(this);
    }

    public synchronized void setTitle(@NotNull String title) {
        if (this.title.equals(title) || title.isEmpty() || title.isBlank()) return;

        this.title = title;

        if (!this.opened) {
            this.actionsAfterOpening.add(() -> InventoryUtils.changeTitle(this.inventory, title));
        } else {
            InventoryUtils.changeTitle(this.inventory, title);
        }
    }

    public synchronized void setGlobalCacheKey(@NotNull String globalCacheKey) {
        boolean putOld = !this.globalCacheKey.isEmpty() && !this.globalCacheKey.isBlank();
        if (this.globalCacheKey.equals(globalCacheKey) || globalCacheKey.isEmpty() || globalCacheKey.isBlank()) return;

        Map<String, Object> oldCache = (putOld) ? this.cache.getCache() : null;

        this.globalCacheKey = globalCacheKey;

        if (putOld) {
            this.cache.getCache().putAll(oldCache);
        }
    }

    public @Nullable MenuItem getContent(@NotNull SlotPos slotPos) {
        int row = slotPos.getRow();
        int column = slotPos.getColumn();
        if (row < 0 || row >= this.rows || column < 0 || column >= this.columns) return null;

        return this.contents[row][column];
    }

    public boolean fits(int slot) {
        return this.menuType.fitsInMenu(slot);
    }
}