package nl.odalitadevelopments.menus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
@Setter
public final class MenuSession {

    private final OdalitaMenus instance;
    private final Player player;

    @Setter(AccessLevel.NONE)
    private SupportedMenuType menuType;
    @Setter(AccessLevel.NONE)
    private Inventory inventory;
    private String title;
    private final MenuContents menuContents;

    private volatile MenuItem[][] contents;
    private volatile boolean hasUpdatableItems = false;

    private String globalCacheKey;
    private final MenuSessionCache cache;

    @Setter(AccessLevel.PACKAGE)
    private boolean opened = false;

    private boolean closed = false;

    MenuSession(OdalitaMenus instance, Player player, SupportedMenuType menuType, Inventory inventory, String title, String globalCacheKey) {
        this.instance = instance;
        this.player = player;
        this.menuType = menuType;

        this.inventory = inventory;
        this.contents = new MenuItem[this.getRows()][this.getColumns()];
        this.title = title;

        this.globalCacheKey = globalCacheKey;
        this.cache = new MenuSessionCache(this);

        this.menuContents = MenuContents.create(this);
    }

    public synchronized void setTitle(@NotNull String title) {
        if (this.title.equals(title)) return;

        this.title = this.instance.getProvidersContainer().getColorProvider().handle(title);

        InventoryUtils.changeTitle(this.inventory, title);
    }

    public void setMenuType(@NotNull MenuType menuType) {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Cannot change menu type asynchronous!");
        }

        if (this.menuType.type().equals(menuType)) return;

        if (this.opened) {
            throw new IllegalStateException("Cannot change menu type while menu is opened!");
        } else {
            this.menuType = this.instance.getSupportedMenuTypes().getSupportedMenuType(menuType);
            this.inventory = this.menuType.createInventory(this.title);
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
        if (row < 0 || row >= this.getRows() || column < 0 || column >= this.getColumns()) return null;

        return this.contents[row][column];
    }

    public boolean fits(int slot) {
        return this.menuType.fitsInMenu(slot);
    }

    @ApiStatus.Internal
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getRows() {
        return Math.max(this.menuType.maxRows(), 1);
    }

    public int getColumns() {
        return this.menuType.maxColumns() + this.menuType.otherSlots().size();
    }
}