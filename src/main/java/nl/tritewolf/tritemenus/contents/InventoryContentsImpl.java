package nl.tritewolf.tritemenus.contents;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.*;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.menu.PlaceableItemAction;
import nl.tritewolf.tritemenus.menu.PlaceableItemsCloseAction;
import nl.tritewolf.tritemenus.menu.type.SupportedFeatures;
import nl.tritewolf.tritemenus.pagination.PaginationBuilder;
import nl.tritewolf.tritemenus.patterns.*;
import nl.tritewolf.tritemenus.scrollable.ScrollableBuilder;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

record InventoryContentsImpl(MenuSession menuSession,
                             InventoryContentsScheduler scheduler) implements InventoryContents {

    @Override
    public void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        this.set(slotPos, item, override, (slot) -> {
            this.menuSession.getContents()[slot.getRow()][slot.getColumn()] = item;
        });
    }

    @Override
    public void set(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.set(slotPos, item, true);
    }

    @Override
    public void set(int row, int column, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(row, column), item, override);
    }

    @Override
    public void set(int row, int column, @NotNull MenuItem item) {
        this.set(row, column, item, true);
    }

    @Override
    public void set(int slot, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(slot), item, override);
    }

    @Override
    public void set(int slot, @NotNull MenuItem item) {
        this.set(slot, item, true);
    }

    @Override
    public void add(@NotNull MenuItem item) {
        this.firstEmptySlot().ifPresent((slotPos) -> {
            this.set(slotPos, item);
        });
    }

    @Override
    public @NotNull Optional<@NotNull SlotPos> firstEmptySlot() {
        for (int row = 0; row < this.menuSession.getRows(); row++) {
            for (int column = 0; column < this.menuSession.getColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);

                if (this.menuSession.getContent(slotPos) == null) {
                    return Optional.of(slotPos);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void fillRow(int row, @NotNull MenuItem item) {
        if (row < 0 || row >= this.menuSession.getRows()) {
            throw new IllegalArgumentException("Row must be between '0' and '" + this.menuSession.getRows() + "'");
        }

        for (int column = 0; column < this.menuSession.getColumns(); column++) {
            this.set(row, column, item);
        }
    }

    @Override
    public void fillColumn(int column, @NotNull MenuItem item) {
        if (column < 0 || column >= this.menuSession.getColumns()) {
            throw new IllegalArgumentException("Column must be between '0' and '" + this.menuSession.getColumns() + "'");
        }

        for (int row = 0; row < this.menuSession.getRows(); row++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    @Override
    public void fillRectangle(@NotNull SlotPos fromPos, @NotNull SlotPos toPos, @NotNull MenuItem item) {
        this.fillRectangle(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    @Override
    public void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, @NotNull MenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;

                this.set(SlotPos.of(row, column), item);
            }
        }
    }

    @Override
    public void fillRectangle(int fromSlot, int toSlot, @NotNull MenuItem item) {
        this.fillRectangle(SlotPos.of(fromSlot), SlotPos.of(toSlot), item);
    }

    @Override
    public void fillBorders(@NotNull MenuItem item) {
        this.fillRectangle(0, 0, this.menuSession.getRows() - 1, this.menuSession.getColumns() - 1, item);
    }

    @Override
    public void fill(@NotNull MenuItem item) {
        for (int row = 0; row < this.menuSession.getRows(); row++) {
            for (int column = 0; column < this.menuSession.getColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);
                if (this.menuSession.getContent(slotPos) != null || this.menuSession.getCache().getPlaceableItems().contains(slotPos.getSlot())) {
                    continue;
                }

                this.set(slotPos, item);
            }
        }
    }

    @Override
    public void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        this.set(slotPos, item, override, (slot) -> {
            this.menuSession.getContents()[slot.getRow()][slot.getColumn()] = item;
            InventoryUtils.updateItem(this.menuSession.getPlayer(), slot.getSlot(), item.getItemStack(), this.menuSession.getInventory());
        });
    }

    @Override
    public void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.setAsync(slotPos, item, true);
    }

    @Override
    public void setAsync(int row, int column, @NotNull MenuItem item, boolean override) {
        this.setAsync(SlotPos.of(row, column), item, override);
    }

    @Override
    public void setAsync(int row, int column, @NotNull MenuItem item) {
        this.setAsync(row, column, item, true);
    }

    @Override
    public void setAsync(int slot, @NotNull MenuItem item, boolean override) {
        this.setAsync(SlotPos.of(slot), item, override);
    }

    @Override
    public void setAsync(int slot, @NotNull MenuItem item) {
        this.setAsync(slot, item, true);
    }

    @Override
    public void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        MenuItem menuItem = item.get();
        if (menuItem instanceof UpdatableItem) {
            throw new IllegalArgumentException("Updatable item cannot be set as refreshable");
        }

        this.set(slotPos, menuItem, override);
        this.menuSession.getCache().getRefreshableItems().put(slotPos.getSlot(), item);
    }

    @Override
    public void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(slotPos, item, true);
    }

    @Override
    public void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.setRefreshable(SlotPos.of(row, column), item, override);
    }

    @Override
    public void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(row, column, item, true);
    }

    @Override
    public void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.setRefreshable(SlotPos.of(slot), item, override);
    }

    @Override
    public void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item) {
        this.setRefreshable(slot, item, true);
    }

    @Override
    public synchronized void refreshItem(@NotNull SlotPos slotPos) {
        this.refreshItem(slotPos.getSlot());
    }

    @Override
    public synchronized void refreshItem(int row, int column) {
        this.refreshItem(SlotPos.of(row, column));
    }

    @Override
    public synchronized void refreshItem(int slot) {
        Supplier<MenuItem> menuItemSupplier = this.menuSession.getCache().getRefreshableItems().get(slot);
        if (menuItemSupplier == null) return;

        this.setAsync(slot, menuItemSupplier.get());
    }

    @Override
    public synchronized void refreshItems(SlotPos @NotNull ... slotPosses) {
        for (SlotPos slotPos : slotPosses) {
            this.refreshItem(slotPos.getSlot());
        }
    }

    @Override
    public synchronized void refreshItems(int @NotNull ... slots) {
        for (int slot : slots) {
            this.refreshItem(slot);
        }
    }

    @Override
    public void setDisplay(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        this.set(slotPos, DisplayItem.of(itemStack));
    }

    @Override
    public void setDisplay(int row, int column, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(row, column), itemStack);
    }

    @Override
    public void setDisplay(int slot, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(slot), itemStack);
    }

    @Override
    public void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material) {
        this.setDisplay(slotPos, new ItemStack(material));
    }

    @Override
    public void setDisplay(int row, int column, @NotNull Material material) {
        this.setDisplay(SlotPos.of(row, column), material);
    }

    @Override
    public void setDisplay(int slot, @NotNull Material material) {
        this.setDisplay(SlotPos.of(slot), material);
    }

    @Override
    public void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(slotPos, InventoryUtils.createItemStack(material, displayName));
    }

    @Override
    public void setDisplay(int row, int column, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(row, column), material, displayName);
    }

    @Override
    public void setDisplay(int slot, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(slot), material, displayName);
    }

    @Override
    public void setClickable(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, ClickableItem.of(itemStack, event));
    }

    @Override
    public void setClickable(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), itemStack, event);
    }

    @Override
    public void setClickable(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), itemStack, event);
    }

    @Override
    public void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, new ItemStack(material), event);
    }

    @Override
    public void setClickable(int row, int column, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, event);
    }

    @Override
    public void setClickable(int slot, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, event);
    }

    @Override
    public void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, InventoryUtils.createItemStack(material, displayName), event);
    }

    @Override
    public void setClickable(int row, int column, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, displayName, event);
    }

    @Override
    public void setClickable(int slot, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, displayName, event);
    }

    @Override
    public void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, UpdatableItem.of(itemStack, event));
    }

    @Override
    public void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event);
    }

    @Override
    public void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event);
    }

    @Override
    public void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.set(slotPos, UpdatableItem.of(itemStack, event, time));
    }

    @Override
    public void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event, time);
    }

    @Override
    public void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event, time);
    }

    @Override
    public @NotNull MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        MenuIterator menuIterator = new MenuIterator(menuIteratorType, this, startRow, startColumn);
        this.menuSession.getCache().getIterators().put(iterator, menuIterator);
        return menuIterator;
    }

    @Override
    public void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn, @NotNull List<@NotNull MenuItem> menuItems,
                                     int... blacklisted) {
        MenuIterator menuIterator = new MenuIterator(menuIteratorType, this, startRow, startColumn);
        menuIterator.blacklist(blacklisted);

        for (MenuItem menuItem : menuItems) {
            menuIterator.setNext(menuItem);
        }
    }

    @Override
    public <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems) {
        MenuIterator value = new MenuIterator(MenuIteratorType.PATTERN, this, 0, 0);
        iteratorPattern.handle(value);

        for (MenuItem menuItem : menuItems) {
            value.setNext(menuItem);
        }
    }

    @Override
    public void createPatternIterator(@NotNull Class<? extends IteratorPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getInstance().getPatternContainer();
        IteratorPattern iteratorPatternByClass = patternContainer.getPattern(clazz);

        if (iteratorPatternByClass == null) {
            throw new IllegalArgumentException("The pattern class '" + clazz.getName() + "' is not registered!");
        }

        this.createPatternIterator(iteratorPatternByClass, menuItems);
    }

    @Override
    public void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getInstance().getPatternContainer();
        List<SlotPos> directionsPattern = patternContainer.getPattern(clazz);
        if (directionsPattern == null) {
            throw new IllegalArgumentException("The pattern class '" + clazz.getName() + "' is not registered!");
        }

        for (int i = 0; i < menuItems.size(); i++) {
            if (i >= directionsPattern.size() || directionsPattern.get(i).getRow() > this.menuSession.getRows()) break;
            this.set(directionsPattern.get(i), menuItems.get(i));
        }
    }

    @Override
    public void registerPlaceableItemSlots(int... slots) {
        for (int slot : slots) {
            this.menuSession.getCache().getPlaceableItems().add(slot);
        }
    }

    @Override
    public void onPlaceableItemClick(@NotNull PlaceableItemAction action) {
        this.menuSession.getCache().setPlaceableItemAction(action);
    }

    @Override
    public void removePlaceableItems(@NotNull PlaceableItemsCloseAction action) {
        this.menuSession.getCache().setPlaceableItemsCloseAction(action);
    }

    @Override
    public @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator) {
        return this.pagination(id, itemsPerPage)
                .iterator(iterator);
    }

    @Override
    public @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage) {
        if (!this.menuSession.getMenuType().isFeatureAllowed(SupportedFeatures.PAGINATION)) {
            throw new IllegalStateException("The menu type '" + this.menuSession.getMenuType().type() + "' does not support pagination!");
        }

        return PaginationBuilder.builder(this, id, itemsPerPage);
    }

    @Override
    public @NotNull ScrollableBuilder scrollable(@NotNull String id, int showYAxis, int showXAxis) {
        if (!this.menuSession.getMenuType().isFeatureAllowed(SupportedFeatures.SCROLLABLE)) {
            throw new IllegalStateException("The menu type '" + this.menuSession.getMenuType().type() + "' does not support scrollable!");
        }

        return ScrollableBuilder.builder(this, id, showYAxis, showXAxis);
    }

    @Override
    public void setPageSwitchUpdateItem(@NotNull SlotPos slotPos, @NotNull PageUpdatableItem menuItem) {
        if (!this.menuSession.getMenuType().isFeatureAllowed(SupportedFeatures.PAGINATION)
                && !this.menuSession.getMenuType().isFeatureAllowed(SupportedFeatures.SCROLLABLE)) {
            throw new IllegalStateException("The menu type '" + this.menuSession.getMenuType().type() + "' does not support pagination and scrollable!");
        }

        this.set(slotPos, menuItem, true);
    }

    @Override
    public void setPageSwitchUpdateItem(int row, int column, @NotNull PageUpdatableItem menuItem) {
        this.setPageSwitchUpdateItem(SlotPos.of(row, column), menuItem);
    }

    @Override
    public void setPageSwitchUpdateItem(int slot, @NotNull PageUpdatableItem menuItem) {
        this.setPageSwitchUpdateItem(SlotPos.of(slot), menuItem);
    }

    @Override
    public synchronized <T> T cache(@NotNull String key, T def) {
        return this.menuSession.getCache().cache(key, def);
    }

    @Override
    public synchronized <T> T cache(@NotNull String key) {
        return this.menuSession.getCache().cache(key);
    }

    @Override
    public synchronized @NotNull InventoryContents setCache(@NotNull String key, @NotNull Object value) {
        this.menuSession.getCache().setCache(key, value);
        return this;
    }

    @Override
    public synchronized @NotNull InventoryContents pruneCache(@NotNull String key) {
        this.menuSession.getCache().pruneCache(key);
        return this;
    }

    @Override
    public synchronized void setTitle(@NotNull String title) {
        this.menuSession.setTitle(title);
    }

    @Override
    public void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action) {
        this.menuSession.getCache().setPlaceableItemsCloseAction(action);
        player.closeInventory();
    }

    @Override
    public @Nullable String getSearchQuery(@NotNull String id) {
        return this.menuSession.getCache().getSearchQueries().get(id);
    }

    private void set(SlotPos slotPos, MenuItem item, boolean override, Consumer<SlotPos> setter) {
        slotPos = this.createSlotPos(slotPos);

        if (!this.menuSession.fits(slotPos.getSlot())) {
            throw new IllegalArgumentException("The slot '" + slotPos.getSlot() + "' is out of bounds for this menu");
        }

        if (!override && this.menuSession.getContent(slotPos) != null) return;

        if (item instanceof PageUpdatableItem) {
            this.menuSession.getCache().getPageSwitchUpdateItems().put(slotPos.getSlot(), () -> item);
        }

        if (!this.menuSession.isHasUpdatableItems() && item.isUpdatable()) {
            this.menuSession.setHasUpdatableItems(true);
        }

        setter.accept(slotPos);
    }

    private SlotPos createSlotPos(SlotPos slotPos) {
        return SlotPos.of(this.menuSession.getMenuType().maxRows(), this.menuSession.getMenuType().maxColumns(), slotPos.getSlot());
    }
}