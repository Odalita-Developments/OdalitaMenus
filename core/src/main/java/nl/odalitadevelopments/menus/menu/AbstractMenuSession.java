package nl.odalitadevelopments.menus.menu;

import com.google.common.collect.Sets;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.InventoryCreation;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.nms.utils.OdalitaLogger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public sealed abstract class AbstractMenuSession<S extends AbstractMenuSession<S, V, C>, V, C extends IMenuContents> permits MenuSession, MenuIdentitySession {

    protected final OdalitaMenus instance;
    final MenuOpenerBuilderImpl<?> builder;

    protected final MenuData data;
    protected V viewerEntry;

    protected InventoryCreation inventoryData;
    protected final C menuContents;

    protected volatile MenuItem[][] contents;
    protected final MenuSessionCache cache;

    protected volatile boolean hasUpdatableItems = false;

    protected volatile boolean initialized = false;
    protected volatile boolean opened = false;
    protected volatile boolean closed = false;

    protected final Set<Runnable> openActions = Sets.newConcurrentHashSet();

    AbstractMenuSession(OdalitaMenus instance, MenuOpenerBuilderImpl<?> builder, MenuData data, InventoryCreation inventoryData, Function<S, C> menuContents) {
        this.instance = instance;
        this.builder = builder;

        this.data = data;

        this.inventoryData = inventoryData;
        this.menuContents = menuContents.apply(this.self());

        this.contents = new MenuItem[this.rows()][this.columns()];
        this.cache = new MenuSessionCache(instance, data);
    }

    public final @NotNull OdalitaMenus instance() {
        return this.instance;
    }

    public final @NotNull MenuData data() {
        return this.data;
    }

    public final @NotNull Inventory inventory() {
        return this.inventoryData.bukkitInventory();
    }

    public final @NotNull C menuContents() {
        return this.menuContents;
    }

    public final MenuItem[][] contents() {
        synchronized (this) {
            return this.contents;
        }
    }

    public final @NotNull MenuSessionCache cache() {
        return this.cache;
    }

    public final boolean hasUpdatableItems() {
        synchronized (this) {
            return this.hasUpdatableItems;
        }
    }

    public final boolean isInitialized() {
        boolean initialized = this.initialized;
        if (!initialized) {
            synchronized (this) {
                initialized = this.initialized;
            }
        }
        return initialized;
    }

    public final boolean isOpened() {
        boolean opened = this.opened;
        if (!opened) {
            synchronized (this) {
                opened = this.opened;
            }
        }
        return opened;
    }

    public final boolean isClosed() {
        boolean closed = this.closed;
        if (!closed) {
            synchronized (this) {
                closed = this.closed;
            }
        }
        return closed;
    }

    public final Set<Runnable> openActions() {
        return this.openActions;
    }

    public final int rows() {
        SupportedMenuType menuType = this.data.getOrThrow(MenuData.Key.TYPE);
        return Math.max(menuType.maxRows(), 1);
    }

    public final int columns() {
        SupportedMenuType menuType = this.data.getOrThrow(MenuData.Key.TYPE);
        return menuType.maxColumns() + menuType.otherSlots().size();
    }

    public final @Nullable MenuItem content(@NotNull SlotPos slotPos) {
        int row = slotPos.getRow();
        int column = slotPos.getColumn();
        if (row < 0 || row >= this.rows() || column < 0 || column >= this.columns()) return null;

        return this.contents()[row][column];
    }

    public final boolean fits(@NotNull SlotPos slot) {
        SupportedMenuType menuType = this.data.getOrThrow(MenuData.Key.TYPE);
        return menuType.fitsInMenu(slot.getSlot());
    }

    /* INTERNAL METHODS */

    final void markAsInitialized() {
        synchronized (this) {
            this.initialized = true;
        }
    }

    final void markAsOpened() {
        synchronized (this) {
            this.opened = true;

            for (Runnable action : this.openActions) {
                action.run();
            }

            this.openActions.clear();
        }
    }

    @ApiStatus.Internal
    public final void markAsClosed() {
        synchronized (this) {
            this.closed = true;
            this.opened = false;
        }
    }

    @ApiStatus.Internal
    public final void hasUpdatableItems(boolean hasUpdatableItems) {
        synchronized (this) {
            this.hasUpdatableItems = hasUpdatableItems;
        }
    }

    @ApiStatus.Internal
    public final void reopen() {
        this.builder.open();
    }

    @ApiStatus.Internal
    public final void id(@NotNull String id) {
        String currentValue = this.data.get(MenuData.Key.ID);
        if (currentValue == null || !currentValue.equals(id)) {
            this.data.set(MenuData.Key.ID, id);
        }
    }

    @ApiStatus.Internal
    public final synchronized void title(@NotNull String title) {
        String currentValue = this.data.get(MenuData.Key.TITLE);
        if (currentValue != null && currentValue.equals(title)) return;

        String newTitle = this.instance.getProvidersContainer().getColorProvider().handle(title);
        this.data.set(MenuData.Key.TITLE, newTitle);

        try {
            OdalitaMenusNMS.getInstance().changeInventoryTitle(this.inventory(), newTitle);
        } catch (Exception exception) {
            OdalitaLogger.error(exception);
            this.data.set(MenuData.Key.TITLE, currentValue);
        }
    }

    @ApiStatus.Internal
    public void menuType(@NotNull MenuType menuType) {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Cannot change menu type asynchronous!");
        }

        SupportedMenuType currentValue = this.data.get(MenuData.Key.TYPE);
        if (currentValue != null && currentValue.type().equals(menuType)) return;

        if (this.opened) {
            throw new IllegalStateException("Cannot change menu type while menu is opened!");
        } else {
            SupportedMenuType newType = this.instance.getSupportedMenuTypes().getSupportedMenuType(menuType);
            this.data.set(MenuData.Key.TYPE, newType);

            // Make sure the contents array is the correct size
            MenuItem[][] newContents = new MenuItem[this.rows()][this.columns()];
            for (int i = 0; i < this.contents.length; i++) {
                if (i >= newContents.length) break;

                System.arraycopy(this.contents[i], 0, newContents[i], 0, Math.min(this.contents[i].length, newContents[i].length));
            }

            this.contents = newContents;

            this.inventoryData = this.createInventoryData(menuType, this.data.getOrThrow(MenuData.Key.TITLE));
        }
    }

    @ApiStatus.Internal
    public final void menuProperty(@NotNull MenuProperty property, int value) {
        SupportedMenuType menuType = this.data.get(MenuData.Key.TYPE);
        if (menuType == null) {
            throw new UnsupportedOperationException("Cannot set property for a menu without a type!");
        }

        if (menuType.type() != property.getMenuType()) {
            throw new UnsupportedOperationException("Can't set property for a '" + property.getMenuType() + "' inventory in a '" + menuType.type() + "' inventory.");
        }

        if (!this.isOpened()) {
            this.openActions.add(() -> OdalitaMenusNMS.getInstance().setInventoryProperty(this.inventory(), property.getIndex(), value));
        } else {
            OdalitaMenusNMS.getInstance().setInventoryProperty(this.inventory(), property.getIndex(), value);
        }
    }

    @ApiStatus.Internal
    public final synchronized void globalCacheKey(@NotNull String globalCacheKey) {
        String currentValue = this.data.get(MenuData.Key.GLOBAL_CACHE_KEY);

        boolean putOld = currentValue != null && !currentValue.isEmpty() && !currentValue.isBlank();
        if ((currentValue != null && currentValue.equals(globalCacheKey)) || globalCacheKey.isEmpty() || globalCacheKey.isBlank()) {
            return;
        }

        this.data.set(MenuData.Key.GLOBAL_CACHE_KEY, globalCacheKey);

        if (putOld) {
            Map<String, Object> oldCache = this.cache.getCache();
            this.cache.getCache().putAll(oldCache);
        }
    }

    /* ABSTRACT METHODS */

    protected abstract @NotNull S self();

    protected abstract @NotNull InventoryCreation createInventoryData(@NotNull MenuType menuType, @NotNull String title);
}