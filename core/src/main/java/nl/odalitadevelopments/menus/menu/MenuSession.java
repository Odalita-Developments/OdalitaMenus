package nl.odalitadevelopments.menus.menu;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.InventoryCreation;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public final class MenuSession {

    private final OdalitaMenus instance;
    @Getter(AccessLevel.NONE)
    private final MenuOpenerBuilderImpl<?> builder;
    private final Player player;

    private String id;
    private SupportedMenuType menuType;
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.NONE)
    private InventoryCreation inventoryData;
    private String title;
    private final MenuContents menuContents;

    @Getter(AccessLevel.NONE)
    public volatile MenuItem[][] contents;
    private volatile boolean hasUpdatableItems = false;

    private String globalCacheKey;
    private final MenuSessionCache cache;

    private volatile boolean initialized = false;
    private volatile boolean opened = false;
    private volatile boolean closed = false;

    private final Collection<Runnable> openActions = Sets.newConcurrentHashSet();

    MenuSession(OdalitaMenus instance, MenuOpenerBuilderImpl<?> builder, Player player, String id, SupportedMenuType menuType, InventoryCreation inventoryData, String title, String globalCacheKey) {
        this.instance = instance;
        this.builder = builder;
        this.player = player;

        this.id = id;
        this.menuType = menuType;

        this.inventoryData = inventoryData;
        this.contents = new MenuItem[this.getRows()][this.getColumns()];
        this.title = title;

        this.globalCacheKey = globalCacheKey;
        this.cache = new MenuSessionCache(this);

        this.menuContents = MenuContents.create(this);
    }

    void initialized() {
        this.initialized = true;
    }

    void opened() {
        this.opened = true;

        for (Runnable action : this.openActions) {
            action.run();
        }

        this.openActions.clear();
    }

    public void setId(@NotNull String id) {
        if (this.id != null && this.id.equals(id)) return;

        this.id = id;
    }

    public @NotNull Inventory getInventory() {
        return this.inventoryData.bukkitInventory();
    }

    public synchronized void setTitle(@NotNull String title) {
        if (this.title.equals(title)) return;

        this.title = this.instance.getProvidersContainer().getColorProvider().handle(title);

        InventoryUtils.changeTitle(this.getInventory(), title);
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

            // Make sure the contents array is the correct size
            MenuItem[][] newContents = new MenuItem[this.getRows()][this.getColumns()];
            System.arraycopy(this.contents, 0, newContents, 0, this.contents.length);
            this.contents = newContents;

            this.inventoryData = this.menuType.createInventory(this.player, this.title);
        }
    }

    public synchronized void setMenuProperty(@NotNull MenuProperty property, int value) {
        if (this.menuType.type() != property.getMenuType()) {
            throw new UnsupportedOperationException("Can't set property for a '" + property.getMenuType() + "' inventory in a '" + this.menuType.type() + "' inventory.");
        }

        if (!this.opened) {
            this.openActions.add(() -> InventoryUtils.setProperty(this.getInventory(), property, value));
        } else {
            InventoryUtils.setProperty(this.getInventory(), property, value);
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

    @ApiStatus.Internal
    public void reopen() {
        this.builder.open();
    }

    public int getRows() {
        return Math.max(this.menuType.maxRows(), 1);
    }

    public int getColumns() {
        return this.menuType.maxColumns() + this.menuType.otherSlots().size();
    }
}