package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.*;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.patterns.*;
import nl.odalitadevelopments.menus.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

interface IMenuContentsImpl extends IMenuContents {

    AbstractMenuSession<?, ?, ?> menuSession();

    @NotNull
    MenuSessionCache cache();

    @Override
    default void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        if (this.menuSession().isInitialized() && !this.menuSession().isOpened()) {
            SlotPos calculateSlotPos = this.calculateSlotPos(slotPos);
            this.menuSession().openActions().add(() -> {
                this.set0(calculateSlotPos, slotPos.getSlot(), item, override, true);
            });
            return;
        }

        this.set0(slotPos, item, override, false);
    }

    @Override
    default void set(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.set(slotPos, item, true);
    }

    @Override
    default void set(int row, int column, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(row, column), item, override);
    }

    @Override
    default void set(int row, int column, @NotNull MenuItem item) {
        this.set(row, column, item, true);
    }

    @Override
    default void set(int slot, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(slot), item, override);
    }

    @Override
    default void set(int slot, @NotNull MenuItem item) {
        this.set(slot, item, true);
    }

    @Override
    default void add(@NotNull MenuItem item) {
        this.firstEmptySlot().ifPresent((slotPos) -> {
            this.set(slotPos, item);
        });
    }

    @Override
    default @NotNull Optional<@NotNull SlotPos> firstEmptySlot() {
        for (int row = 0; row < this.maxColumns(); row++) {
            for (int column = 0; column < this.maxColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);

                if (this.menuSession().content(slotPos) == null) {
                    return Optional.of(slotPos);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    default void clear(@NotNull SlotPos slotPos) {
        if (this.menuSession().isInitialized() && !this.menuSession().isOpened()) {
            SlotPos calculateSlotPos = this.calculateSlotPos(slotPos);
            this.menuSession().openActions().add(() -> {
                this.set0(calculateSlotPos, slotPos.getSlot(), null, true, true);
            });
            return;
        }

        this.set0(slotPos, null, true, false);
    }

    @Override
    default void clear(int row, int column) {
        this.clear(SlotPos.of(row, column));
    }

    @Override
    default void clear(int slot) {
        this.clear(SlotPos.of(slot));
    }

    @Override
    default boolean isEmpty(@NotNull SlotPos slotPos) {
        return this.menuSession().content(slotPos) == null;
    }

    @Override
    default boolean isEmpty(int row, int column) {
        return this.isEmpty(SlotPos.of(row, column));
    }

    @Override
    default boolean isEmpty(int slot) {
        return this.isEmpty(SlotPos.of(slot));
    }

    @Override
    default void fillRow(int row, @NotNull MenuItem item) {
        if (row < 0 || row >= this.maxRows()) {
            throw new IllegalArgumentException("Row must be between '0' and '" + this.maxRows() + "'");
        }

        for (int column = 0; column < this.maxColumns(); column++) {
            this.set(row, column, item);
        }
    }

    @Override
    default void fillColumn(int column, @NotNull MenuItem item) {
        if (column < 0 || column >= this.maxColumns()) {
            throw new IllegalArgumentException("Column must be between '0' and '" + this.maxColumns() + "'");
        }

        for (int row = 0; row < this.maxRows(); row++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    @Override
    default void fillRectangle(@NotNull SlotPos fromPos, @NotNull SlotPos toPos, @NotNull MenuItem item) {
        this.fillRectangle(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    @Override
    default void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, @NotNull MenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;

                this.set(SlotPos.of(row, column), item);
            }
        }
    }

    @Override
    default void fillRectangle(int fromSlot, int toSlot, @NotNull MenuItem item) {
        this.fillRectangle(SlotPos.of(fromSlot), SlotPos.of(toSlot), item);
    }

    @Override
    default void fillBorders(@NotNull MenuItem item) {
        this.fillRectangle(0, 0, this.menuSession().rows() - 1, this.menuSession().columns() - 1, item);
    }

    @Override
    default void fill(@NotNull MenuItem item) {
        for (int row = 0; row < this.maxRows(); row++) {
            for (int column = 0; column < this.maxColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);
                if (this.menuSession().content(slotPos) != null || this.cache().getPlaceableItems().contains(slotPos.getSlot())) {
                    continue;
                }

                this.set(slotPos, item);
            }
        }
    }

    @Override
    default void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.set(slotPos, item.get(), override);
        this.cache().getRefreshableItems().put(slotPos.getSlot(), item);
    }

    @Override
    default void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(slotPos, item, true);
    }

    @Override
    default void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.setRefreshable(SlotPos.of(row, column), item, override);
    }

    @Override
    default void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(row, column, item, true);
    }

    @Override
    default void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.setRefreshable(SlotPos.of(slot), item, override);
    }

    @Override
    default void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(slot, item, true);
    }

    @Override
    default void refreshItem(@NotNull SlotPos slotPos) {
        synchronized (this) {
            int slot = slotPos.getSlot();

            Supplier<MenuItem> menuItemSupplier = this.cache().getRefreshableItems().get(slot);
            if (menuItemSupplier == null) {
                MenuItem item = this.menuSession().content(slotPos);
                if (item == null || !item.isUpdatable()) return;

                menuItemSupplier = () -> item;
            }

            this.set(slot, menuItemSupplier.get());
        }
    }

    @Override
    default void refreshItem(int row, int column) {
        this.refreshItem(SlotPos.of(row, column));
    }

    @Override
    default void refreshItem(int slot) {
        this.refreshItem(SlotPos.of(slot));
    }

    @Override
    default void refreshItems(SlotPos @NotNull ... slotPosses) {
        for (SlotPos slotPos : slotPosses) {
            this.refreshItem(slotPos.getSlot());
        }
    }

    @Override
    default void refreshItems(int @NotNull ... slots) {
        for (int slot : slots) {
            this.refreshItem(slot);
        }
    }

    @Override
    default void setDisplay(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        this.set(slotPos, DisplayItem.of(itemStack));
    }

    @Override
    default void setDisplay(int row, int column, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(row, column), itemStack);
    }

    @Override
    default void setDisplay(int slot, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(slot), itemStack);
    }

    @Override
    default void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material) {
        this.setDisplay(slotPos, new ItemStack(material));
    }

    @Override
    default void setDisplay(int row, int column, @NotNull Material material) {
        this.setDisplay(SlotPos.of(row, column), material);
    }

    @Override
    default void setDisplay(int slot, @NotNull Material material) {
        this.setDisplay(SlotPos.of(slot), material);
    }

    @Override
    default void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(slotPos, ItemUtils.createItemStack(material, displayName));
    }

    @Override
    default void setDisplay(int row, int column, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(row, column), material, displayName);
    }

    @Override
    default void setDisplay(int slot, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(slot), material, displayName);
    }

    @Override
    default void setClickable(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, ClickableItem.of(itemStack, event));
    }

    @Override
    default void setClickable(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), itemStack, event);
    }

    @Override
    default void setClickable(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), itemStack, event);
    }

    @Override
    default void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, new ItemStack(material), event);
    }

    @Override
    default void setClickable(int row, int column, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, event);
    }

    @Override
    default void setClickable(int slot, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, event);
    }

    @Override
    default void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, ItemUtils.createItemStack(material, displayName), event);
    }

    @Override
    default void setClickable(int row, int column, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, displayName, event);
    }

    @Override
    default void setClickable(int slot, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, displayName, event);
    }

    @Override
    default void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, UpdatableItem.of(itemStack, event));
    }

    @Override
    default void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event);
    }

    @Override
    default void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event);
    }

    @Override
    default void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.set(slotPos, UpdatableItem.of(itemStack, event, time));
    }

    @Override
    default void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event, time);
    }

    @Override
    default void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event, time);
    }

    @Override
    default void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, int time) {
        this.set(slotPos, UpdatableItem.of(itemStack, time));
    }

    @Override
    default void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, int time) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, time);
    }

    @Override
    default void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, int time) {
        this.setUpdatable(SlotPos.of(slot), itemStack, time);
    }

    @Override
    default void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack) {
        this.set(slotPos, UpdatableItem.of(itemStack));
    }

    @Override
    default void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack) {
        this.setUpdatable(SlotPos.of(row, column), itemStack);
    }

    @Override
    default void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack) {
        this.setUpdatable(SlotPos.of(slot), itemStack);
    }

    @Override
    default @NotNull MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        MenuIterator menuIterator = new MenuIterator(this.menuSession(), null, menuIteratorType, startRow, startColumn);
        this.cache().getIterators().put(iterator, menuIterator);
        return menuIterator;
    }

    @Override
    default void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn, @NotNull List<@NotNull MenuItem> menuItems,
                                      int... blacklisted) {
        MenuIterator menuIterator = new MenuIterator(this.menuSession(), null, menuIteratorType, startRow, startColumn);
        menuIterator.blacklist(blacklisted);

        for (MenuItem menuItem : menuItems) {
            menuIterator.setNext(menuItem);
        }
    }

    @Override
    default @NotNull <T> MenuObjectIterator<T> createObjectIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                                                                    @NotNull Class<T> clazz,
                                                                    @NotNull Function<@NotNull T, @NotNull MenuItem> menuItemCreatorFunction) {
        return new MenuObjectIterator<>(this.menuSession(), null, menuIteratorType, startRow, startColumn, menuItemCreatorFunction);
    }

    @Override
    default <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems) {
        MenuIterator value = new MenuIterator(this.menuSession(), null, MenuIteratorType.PATTERN, 0, 0);
        iteratorPattern.handle(value);

        for (MenuItem menuItem : menuItems) {
            value.setNext(menuItem);
        }
    }

    @Override
    default void createPatternIterator(@NotNull Class<? extends IteratorPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = this.menuSession().instance().getPatternContainer();
        IteratorPattern iteratorPatternByClass = patternContainer.getPattern(clazz);

        if (iteratorPatternByClass == null) {
            throw new IllegalArgumentException("The pattern class '" + clazz.getName() + "' is not registered!");
        }

        this.createPatternIterator(iteratorPatternByClass, menuItems);
    }

    @Override
    default void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = this.menuSession().instance().getPatternContainer();
        List<SlotPos> directionsPattern = patternContainer.getPattern(clazz);
        if (directionsPattern == null) {
            throw new IllegalArgumentException("The pattern class '" + clazz.getName() + "' is not registered!");
        }

        for (int i = 0; i < menuItems.size(); i++) {
            if (i >= directionsPattern.size() || directionsPattern.get(i).getRow() > this.menuSession().rows()) break;
            this.set(directionsPattern.get(i), menuItems.get(i));
        }
    }

    @Override
    default <T> T cache(@NotNull String key, T def) {
        synchronized (this.cache()) {
            return this.cache().cache(key, def);
        }
    }

    @Override
    default <T> T cache(@NotNull String key) {
        synchronized (this.cache()) {
            return this.cache().cache(key);
        }
    }

    @Override
    default @NotNull IMenuContents setCache(@NotNull String key, @NotNull Object value) {
        synchronized (this.cache()) {
            this.cache().setCache(key, value);
            return this;
        }
    }

    @Override
    default @NotNull IMenuContents pruneCache(@NotNull String key) {
        synchronized (this) {
            this.cache().pruneCache(key);
            return this;
        }
    }

    @Override
    default void setGlobalCacheKey(@NotNull String key) {
        this.menuSession().globalCacheKey(key);
    }

    @Override
    default void setId(@NotNull String id) {
        this.menuSession().id(id);
    }

    @Override
    default void setTitle(@NotNull String title) {
        this.menuSession().title(title);
    }

    @Override
    default void setMenuType(@NotNull MenuType menuType) {
        this.menuSession().menuType(menuType);
    }

    default void set0(SlotPos slotPos, int originalSlot, MenuItem item, boolean override, boolean calculated) {
        // Don't set items after the menu is closed
        if (this.menuSession().isClosed()) return;

        if (!calculated) slotPos = this.calculateSlotPos(slotPos);
        int slot = slotPos.getSlot();

        if (!this.menuSession().fits(slotPos)) {
            throw new IllegalArgumentException("The slot '" + slot + "' is out of bounds for this menu");
        }

        if (!override && this.menuSession().content(slotPos) != null) return;

        if (item != null && !this.menuSession().hasUpdatableItems() && item.isUpdatable()) {
            this.menuSession().hasUpdatableItems(true);
        }

        if (item instanceof PageUpdatableItem pageUpdatableItem) {
            this.cache().getPageSwitchUpdateItems().putIfAbsent(originalSlot, () -> pageUpdatableItem);
        }

        this.menuSession().contents()[slotPos.getRow()][slotPos.getColumn()] = item;

        if (this.menuSession().isOpened()) {
            OdalitaMenusNMS.getInstance().setInventoryItem(slot, item == null ? null : item.provideItem(this.menuSession().instance(), this), this.menuSession().inventory());
        }
    }

    default void set0(SlotPos slotPos, MenuItem item, boolean override, boolean calculated) {
        this.set0(slotPos, slotPos.getSlot(), item, override, calculated);
    }

    default SlotPos calculateSlotPos(SlotPos slotPos) {
        return slotPos.convertTo(
                this.maxRows(),
                this.maxColumns()
        );
    }

    @Override
    default int maxRows() {
        return this.menuSession().rows();
    }

    @Override
    default int maxColumns() {
        return this.menuSession().columns();
    }
}