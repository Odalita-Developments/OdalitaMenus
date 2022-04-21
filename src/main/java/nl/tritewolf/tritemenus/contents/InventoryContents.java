package nl.tritewolf.tritemenus.contents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.pagination.Pagination;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.PlaceableItemsCloseAction;
import nl.tritewolf.tritemenus.patterns.DirectionPattern;
import nl.tritewolf.tritemenus.patterns.IteratorPattern;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import nl.tritewolf.tritemenus.patterns.PatternContainer;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class InventoryContents {

    protected final MenuObject triteMenu;
    private final Map<String, MenuIterator> iterators = new HashMap<>();

    public void set(SlotPos slotPos, MenuItem item) {
        this.triteMenu.setHasUpdatableItems(item.isUpdatable());
        this.triteMenu.getContents()[slotPos.getRow()][slotPos.getColumn()] = item;
    }

    public synchronized void setAsync(SlotPos slotPos, MenuItem item) {
        this.triteMenu.setHasUpdatableItems(item.isUpdatable());
        this.triteMenu.getContents()[slotPos.getRow()][slotPos.getColumn()] = item;

        InventoryUtils.updateItem(this.triteMenu.getPlayer(), slotPos.getSlot(), item.getItemStack(), this.triteMenu.getInventory());
    }

    public void set(int row, int column, MenuItem item) {
        this.set(SlotPos.of(row, column), item);
    }

    public void set(int slot, MenuItem item) {
        this.set(SlotPos.of(slot), item);
    }

    public void setClickable(SlotPos slotPos, ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        this.set(slotPos, new ClickableItem(itemStack, event));
    }

    public void setClickable(int row, int column, ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(row, column), new ClickableItem(itemStack, event));
    }

    public void setClickable(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(slot), new ClickableItem(itemStack, event));
    }

    public void setClickable(SlotPos slotPos, Material material, Consumer<InventoryClickEvent> event) {
        this.set(slotPos, new ClickableItem(new ItemStack(material), event));
    }

    public void setClickable(int row, int column, Material material, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(row, column), new ClickableItem(new ItemStack(material), event));
    }

    public void setClickable(int slot, Material material, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(slot), new ClickableItem(new ItemStack(material), event));
    }

    public void setClickable(SlotPos slotPos, Material material, String displayName, Consumer<InventoryClickEvent> event) {
        this.set(slotPos, new ClickableItem(getItemStack(material, displayName), event));
    }

    public void setClickable(int row, int column, Material material, String displayName, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(row, column), new ClickableItem(getItemStack(material, displayName), event));
    }

    public void setClickable(int slot, Material material, String displayName, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(slot), new ClickableItem(getItemStack(material, displayName), event));
    }

    public void setDisplay(SlotPos slotPos, ItemStack itemStack) {
        this.set(slotPos, new DisplayItem(itemStack));
    }

    public void setDisplay(int row, int column, ItemStack itemStack) {
        this.set(SlotPos.of(row, column), new DisplayItem(itemStack));
    }

    public void setDisplay(int slot, ItemStack itemStack) {
        this.set(SlotPos.of(slot), new DisplayItem(itemStack));
    }

    public void setDisplay(SlotPos slotPos, Material material) {
        this.set(slotPos, new DisplayItem(new ItemStack(material)));
    }

    public void setDisplay(int row, int column, Material material) {
        this.set(SlotPos.of(row, column), new DisplayItem(new ItemStack(material)));
    }

    public void setDisplay(int slot, Material material) {
        this.set(SlotPos.of(slot), new DisplayItem(new ItemStack(material)));
    }

    public void setDisplay(SlotPos slotPos, Material material, String displayName) {
        this.set(slotPos, new DisplayItem(getItemStack(material, displayName)));
    }

    public void setDisplay(int row, int column, Material material, String displayName) {
        this.set(SlotPos.of(row, column), new DisplayItem(getItemStack(material, displayName)));
    }

    public void setDisplay(int slot, Material material, String displayName) {
        this.set(SlotPos.of(slot), new DisplayItem(getItemStack(material, displayName)));
    }

    public void setUpdatable(SlotPos slotPos, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event) {
        this.set(slotPos, new UpdatableItem(itemStack, event));
    }

    public void setUpdatable(int row, int column, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(row, column), new UpdatableItem(itemStack, event));
    }

    public void setUpdatable(int slot, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event) {
        this.set(SlotPos.of(slot), new UpdatableItem(itemStack, event));
    }

    public void setUpdatable(SlotPos slotPos, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event, int time) {
        this.set(slotPos, new UpdatableItem(itemStack, event, time));
    }

    public void setUpdatable(int row, int column, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event, int time) {
        this.set(SlotPos.of(row, column), new UpdatableItem(itemStack, event, time));
    }

    public void setUpdatable(int slot, Supplier<ItemStack> itemStack, Consumer<InventoryClickEvent> event, int time) {
        this.set(SlotPos.of(slot), new UpdatableItem(itemStack, event, time));
    }

    private ItemStack getItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public void add(MenuItem item) {
        this.firstEmptyPosition().ifPresent((slotPos) -> {
            this.set(slotPos, item);
        });
    }

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

    public void fillRow(int row, MenuItem item) {
        if (row >= this.triteMenu.getContents().length) return;

        for (int column = 0; column < 9; column++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    public void fillColumn(int column, MenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            this.set(SlotPos.of(row, column), item);
        }
    }

    public void fillBorders(MenuItem item) {
        fillRectangle(0, 0, this.triteMenu.getRows() - 1, 8, item);
    }

    public void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, MenuItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;

                this.set(SlotPos.of(row, column), item);
            }
        }
    }

    public void fillRectangle(SlotPos fromPos, SlotPos toPos, MenuItem item) {
        this.fillRectangle(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    public void fillRectangle(int fromSlot, int toSlot, MenuItem item) {
        this.fillRectangle(SlotPos.of(fromSlot), SlotPos.of(toSlot), item);
    }

    public void fill(MenuItem item) {
        for (int row = 0; row < this.triteMenu.getRows(); row++) {
            for (int column = 0; column < 9; column++) {
                SlotPos slotPos = SlotPos.of(row, column);
                if (this.triteMenu.getContent(slotPos) != null) continue;

                this.set(slotPos, item);
            }
        }
    }

    public MenuIterator createIterator(String iterator, MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        MenuIterator value = new MenuIterator(this, menuIteratorType, startRow, startColumn, true);
        iterators.put(iterator, value);
        return value;
    }

    public void createSimpleIterator(MenuIteratorType menuIteratorType, int startRow, int startColumn, List<MenuItem> menuItems, int... blacklisted) {
        MenuIterator value = new MenuIterator(this, menuIteratorType, startRow, startColumn, true);
        value.blacklist(blacklisted);

        for (MenuItem menuItem : menuItems) {
            value.next().set(menuItem);
        }
    }

    public void createPatternIterator(MenuPattern iteratorPattern, MenuIteratorType menuIteratorType, List<MenuItem> menuItems) {
        MenuIterator value = new MenuIterator(this, menuIteratorType, 0, 0, true);
        iteratorPattern.handle(value);

        for (MenuItem menuItem : menuItems) {
            value.next().set(menuItem);
        }
    }

    public void createPatternIterator(Class<? extends IteratorPattern> clazz, MenuIteratorType menuIteratorType, List<MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getTriteMenus().getTriteJection(PatternContainer.class);
        MenuPattern iteratorPatternByClass = patternContainer.getIteratorPatternByClass(clazz);

        if (iteratorPatternByClass == null) {
            //todo throw exception
            return;
        }

        createPatternIterator(iteratorPatternByClass, menuIteratorType, menuItems);
    }

    public void createDirectionsPatternIterator(Class<? extends DirectionPattern> clazz, List<MenuItem> menuItems) {
        PatternContainer patternContainer = TriteMenus.getTriteMenus().getTriteJection(PatternContainer.class);
        List<SlotPos> directionsPattern = patternContainer.getDirectionsPatternByClass(clazz);

        for (int i = 0; i < menuItems.size(); i++) {
            if (i >= directionsPattern.size() || directionsPattern.get(i).getRow() > triteMenu.getRows()) break;
            set(directionsPattern.get(i), menuItems.get(i));
        }
    }

    public void registerPlaceableItemSlots(@NotNull Integer... slots) {
        this.triteMenu.setPlaceableItems(Arrays.asList(slots));
    }

    public void removePlaceableItems(PlaceableItemsCloseAction action) {
        this.triteMenu.setPlaceableItemsCloseAction(action);
    }

    public void closeInventory(Player player, PlaceableItemsCloseAction action) {
        this.triteMenu.setPlaceableItemsCloseAction(action);
        player.closeInventory();
    }

    public Pagination pagination(String id, int itemsPerPage, MenuIterator iterator, List<Supplier<MenuItem>> items) {
        Pagination pagination = new Pagination(this, itemsPerPage, iterator, items);
        this.triteMenu.getPaginationMap().put(id, pagination);
        return pagination;
    }

    public Pagination pagination(String id, int itemsPerPage, MenuIterator iterator, Supplier<List<Supplier<MenuItem>>> itemsSupplier) {
        return this.pagination(id, itemsPerPage, iterator, itemsSupplier.get());
    }

    public Pagination pagination(String id, int itemsPerPage, MenuIterator iterator) {
        return this.pagination(id, itemsPerPage, iterator, new ArrayList<>());
    }

    public String getSearchQuery(String id) {
        return this.triteMenu.getSearchQueries().get(id);
    }
}