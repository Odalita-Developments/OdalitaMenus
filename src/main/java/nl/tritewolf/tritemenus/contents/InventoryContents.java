package nl.tritewolf.tritemenus.contents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.PlaceableItemsCloseAction;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.patterns.*;
import nl.tritewolf.tritemenus.scrollable.ScrollableBuilder;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class InventoryContents {

    protected final MenuObject triteMenu;
    protected final Map<String, MenuIterator> iterators = new HashMap<>();

    public void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        if (!override && this.triteMenu.getContent(slotPos) != null) return;

        if (!this.triteMenu.isHasUpdatableItems() && item.isUpdatable()) {
            this.triteMenu.setHasUpdatableItems(true);
        }

        this.triteMenu.getContents()[slotPos.getRow()][slotPos.getColumn()] = item;
    }

    public void set(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.set(slotPos, item, true);
    }

    public synchronized void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        if (!override && this.triteMenu.getContent(slotPos) != null) return;

        if (!this.triteMenu.isHasUpdatableItems() && item.isUpdatable()) {
            this.triteMenu.setHasUpdatableItems(true);
        }

        this.triteMenu.getContents()[slotPos.getRow()][slotPos.getColumn()] = item;

        InventoryUtils.updateItem(this.triteMenu.getPlayer(), slotPos.getSlot(), item.getItemStack(), this.triteMenu.getInventory());
    }

    public synchronized void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.setAsync(slotPos, item, true);
    }

    public void set(int row, int column, @NotNull MenuItem item) {
        this.set(SlotPos.of(row, column), item);
    }

    public void set(int slot, @NotNull MenuItem item) {
        this.set(SlotPos.of(slot), item);
    }

    public void setClickable(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, ClickableItem.of(itemStack, event));
    }

    public void setClickable(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), itemStack, event);
    }

    public void setClickable(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), itemStack, event);
    }

    public void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, new ItemStack(material), event);
    }

    public void setClickable(int row, int column, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, event);
    }

    public void setClickable(int slot, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, event);
    }

    public void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName,
                             @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(slotPos, this.getItemStack(material, displayName), event);
    }

    public void setClickable(int row, int column, @NotNull Material material, @NotNull String displayName,
                             @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(row, column), material, displayName, event);
    }

    public void setClickable(int slot, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setClickable(SlotPos.of(slot), material, displayName, event);
    }

    public void setDisplay(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        this.set(slotPos, DisplayItem.of(itemStack));
    }

    public void setDisplay(int row, int column, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(row, column), itemStack);
    }

    public void setDisplay(int slot, @NotNull ItemStack itemStack) {
        this.setDisplay(SlotPos.of(slot), itemStack);
    }

    public void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material) {
        this.setDisplay(slotPos, new ItemStack(material));
    }

    public void setDisplay(int row, int column, @NotNull Material material) {
        this.setDisplay(SlotPos.of(row, column), material);
    }

    public void setDisplay(int slot, @NotNull Material material) {
        this.setDisplay(SlotPos.of(slot), material);
    }

    public void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(slotPos, this.getItemStack(material, displayName));
    }

    public void setDisplay(int row, int column, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(row, column), material, displayName);
    }

    public void setDisplay(int slot, @NotNull Material material, @NotNull String displayName) {
        this.setDisplay(SlotPos.of(slot), material, displayName);
    }

    public void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.set(slotPos, UpdatableItem.of(itemStack, event));
    }

    public void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event);
    }

    public void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event);
    }

    public void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event,
                             int time) {
        this.set(slotPos, UpdatableItem.of(itemStack, event, time));
    }

    public void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event,
                             int time) {
        this.setUpdatable(SlotPos.of(row, column), itemStack, event, time);
    }

    public void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time) {
        this.setUpdatable(SlotPos.of(slot), itemStack, event, time);
    }

    @Deprecated
    private ItemStack getItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public void add(@NotNull MenuItem item) {
        this.firstEmptyPosition().ifPresent((slotPos) -> {
            this.set(slotPos, item);
        });
    }

    @NotNull
    public Optional<SlotPos> firstEmptyPosition() {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                SlotPos slotPos = SlotPos.of(row, column);

                if (this.triteMenu.getContent(slotPos) == null) {
                    return Optional.of(slotPos);
                }
            }
        }

        return Optional.empty();
    }

    public void fillRow(int row, @NotNull MenuItem item) {
        if (row >= this.triteMenu.getContents().length) return;

        for (int column = 0; column < 9; column++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    public void fillColumn(int column, @NotNull MenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    public void fillBorders(@NotNull MenuItem item) {
        fillRectangle(0, 0, this.triteMenu.getRows() - 1, 8, item);
    }

    public void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, @NotNull MenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;

                this.set(SlotPos.of(row, column), item);
            }
        }
    }

    public void fillRectangle(@NotNull SlotPos fromPos, @NotNull SlotPos toPos, @NotNull MenuItem item) {
        this.fillRectangle(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    public void fillRectangle(int fromSlot, int toSlot, @NotNull MenuItem item) {
        this.fillRectangle(SlotPos.of(fromSlot), SlotPos.of(toSlot), item);
    }

    public void fill(@NotNull MenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                SlotPos slotPos = SlotPos.of(row, column);
                if (this.triteMenu.getContent(slotPos) != null) continue;

                this.set(slotPos, item);
            }
        }
    }

    @NotNull
    public MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        MenuIterator menuIterator = new MenuIterator(menuIteratorType, this, startRow, startColumn);
        this.iterators.put(iterator, menuIterator);
        return menuIterator;
    }

    public void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                                     @NotNull List<@NotNull MenuItem> menuItems, int... blacklisted) {
        MenuIterator menuIterator = new MenuIterator(menuIteratorType, this, startRow, startColumn);
        menuIterator.blacklist(blacklisted);

        for (MenuItem menuItem : menuItems) {
            menuIterator.setNext(menuItem);
        }
    }

    public <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems) {
        MenuIterator value = new MenuIterator(MenuIteratorType.PATTERN, this, 0, 0);
        iteratorPattern.handle(value);

        for (MenuItem menuItem : menuItems) {
            value.setNext(menuItem);
        }
    }

    public void createPatternIterator(@NotNull Class<? extends IteratorPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getTriteMenus().getTriteJection(PatternContainer.class);
        IteratorPattern iteratorPatternByClass = patternContainer.getPattern(clazz);

        if (iteratorPatternByClass == null) {
            // TODO throw exception
            return;
        }

        this.createPatternIterator(iteratorPatternByClass, menuItems);
    }

    public void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getTriteMenus().getTriteJection(PatternContainer.class);
        List<SlotPos> directionsPattern = patternContainer.getPattern(clazz);
        if (directionsPattern == null) {
            // TODO throw exception
            return;
        }

        for (int i = 0; i < menuItems.size(); i++) {
            if (i >= directionsPattern.size() || directionsPattern.get(i).getRow() > this.triteMenu.getRows()) break;
            this.set(directionsPattern.get(i), menuItems.get(i));
        }
    }

    public void registerPlaceableItemSlots(Integer @NotNull ... slots) {
        this.triteMenu.setPlaceableItems(Arrays.asList(slots));
    }

    public void removePlaceableItems(@NotNull PlaceableItemsCloseAction action) {
        this.triteMenu.setPlaceableItemsCloseAction(action);
    }

    public void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action) {
        this.triteMenu.setPlaceableItemsCloseAction(action);
        player.closeInventory();
    }

    @NotNull
    public Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator,
                                 @NotNull List<@NotNull Supplier<@NotNull MenuItem>> items) {
        Pagination pagination = new Pagination(id, this, itemsPerPage, iterator, items);
        this.triteMenu.getPaginationMap().put(id, pagination);
        return pagination;
    }

    @NotNull
    public Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator,
                                 @NotNull Supplier<@NotNull List<@NotNull Supplier<@NotNull MenuItem>>> itemsSupplier) {
        return this.pagination(id, itemsPerPage, iterator, itemsSupplier.get());
    }

    @NotNull
    public Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator) {
        Pagination pagination = new Pagination(id, this, itemsPerPage, iterator);
        this.triteMenu.getPaginationMap().put(id, pagination);
        return pagination;
    }

    public void setPageSwitchUpdateItem(int slot, @NotNull MenuItem menuItem) {
        this.triteMenu.getPageSwitchUpdateItems().put(slot, () -> menuItem);
        this.set(SlotPos.of(slot), menuItem, true);
    }

    public @NotNull ScrollableBuilder scrollable(@NotNull String id, int showYAxis, int showXAxis) {
        return ScrollableBuilder.builder(this, id, showYAxis, showXAxis);
    }

    @Nullable
    public String getSearchQuery(@NotNull String id) {
        return this.triteMenu.getSearchQueries().get(id);
    }
}