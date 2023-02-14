package nl.odalitadevelopments.menus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.InventoryContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class MenuSession {

    private final OdalitaMenus instance;
    private final Player player;

    private final SupportedMenuType menuType;
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

    private boolean closed = false;

    MenuSession(OdalitaMenus instance, Player player, SupportedMenuType menuType, Inventory inventory, String title, String globalCacheKey) {
        this.instance = instance;
        this.player = player;
        this.menuType = menuType;

        this.rows = Math.max(menuType.maxRows(), 1);
        this.columns = menuType.maxColumns() + menuType.otherSlots().size();

        this.inventory = inventory;
        this.contents = new MenuItem[this.rows][this.columns];
        this.title = title;

        this.globalCacheKey = globalCacheKey;
        this.cache = new MenuSessionCache(this);

        this.inventoryContents = InventoryContents.create(this);
    }

    public synchronized void setTitle(@NotNull String title) {
        if (this.title.equals(title)) return;

        this.title = this.instance.getProvidersContainer().getColorProvider().handle(title);

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

    @ApiStatus.Internal
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}