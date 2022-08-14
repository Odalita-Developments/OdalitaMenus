package nl.tritewolf.tritemenus.contents;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.PageUpdatableItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.MenuSession;
import nl.tritewolf.tritemenus.menu.PlaceableItemsCloseAction;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.patterns.DirectionPattern;
import nl.tritewolf.tritemenus.patterns.IteratorPattern;
import nl.tritewolf.tritemenus.patterns.MenuPattern;
import nl.tritewolf.tritemenus.patterns.PatternCache;
import nl.tritewolf.tritemenus.scrollable.ScrollableBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface InventoryContents {

    // Temporary
    @ApiStatus.Internal
    static InventoryContents create(@NotNull MenuSession menuSession) {
        return new InventoryContentsImpl(menuSession, new InventoryContentsSchedulerImpl(menuSession));
    }

    @NotNull
    MenuSession menuSession();

    /* DEFAULT */
    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override);

    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item);

    void set(int row, int column, @NotNull MenuItem item, boolean override);

    void set(int row, int column, @NotNull MenuItem item);

    void set(int slot, @NotNull MenuItem item, boolean override);

    void set(int slot, @NotNull MenuItem item);

    void add(@NotNull MenuItem item);

    @NotNull Optional<@NotNull SlotPos> firstEmptySlot();

    void fillRow(int row, @NotNull MenuItem item);

    void fillColumn(int column, @NotNull MenuItem item);

    void fillRectangle(@NotNull SlotPos fromPos, @NotNull SlotPos toPos, @NotNull MenuItem item);

    void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, @NotNull MenuItem item);

    void fillRectangle(int fromSlot, int toSlot, @NotNull MenuItem item);

    void fillBorders(@NotNull MenuItem item);

    void fill(@NotNull MenuItem item);


    /* DEFAULT ASYNC */
    void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override);

    void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item);

    void setAsync(int row, int column, @NotNull MenuItem item, boolean override);

    void setAsync(int row, int column, @NotNull MenuItem item);

    void setAsync(int slot, @NotNull MenuItem item, boolean override);

    void setAsync(int slot, @NotNull MenuItem item);


    /* DEFAULT DISPLAY */
    void setDisplay(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack);

    void setDisplay(int row, int column, @NotNull ItemStack itemStack);

    void setDisplay(int slot, @NotNull ItemStack itemStack);

    void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material);

    void setDisplay(int row, int column, @NotNull Material material);

    void setDisplay(int slot, @NotNull Material material);

    void setDisplay(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName);

    void setDisplay(int row, int column, @NotNull Material material, @NotNull String displayName);

    void setDisplay(int slot, @NotNull Material material, @NotNull String displayName);

    /* DEFAULT CLICKABLE */
    void setClickable(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int row, int column, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int slot, @NotNull Material material, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(@NotNull SlotPos slotPos, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int row, int column, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setClickable(int slot, @NotNull Material material, @NotNull String displayName, @NotNull Consumer<@NotNull InventoryClickEvent> event);


    /* DEFAULT UPDATABLE */

    void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event);

    void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event,
                      int time);

    void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event,
                      int time);

    void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> event, int time);


    /* ITERATORS */
    @NotNull
    MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn);

    void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                              @NotNull List<@NotNull MenuItem> menuItems, int... blacklisted);

    <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems);

    void createPatternIterator(@NotNull Class<? extends IteratorPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems);

    void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems);


    /* PLACEABLE ITEMS */
    void registerPlaceableItemSlots(int... slots);

    void removePlaceableItems(@NotNull PlaceableItemsCloseAction action);


    /* PAGINATION & SCROLLABLE */
    @NotNull
    Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator,
                          @NotNull List<@NotNull Supplier<@NotNull MenuItem>> items);

    @NotNull
    Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator,
                          @NotNull Supplier<@NotNull List<@NotNull Supplier<@NotNull MenuItem>>> itemsSupplier);

    @NotNull
    Pagination pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator);

    @NotNull ScrollableBuilder scrollable(@NotNull String id,
                                          @MagicConstant(intValues = {1, 2, 3, 4, 5, 6}) int showYAxis,
                                          @MagicConstant(intValues = {1, 2, 3, 4, 5, 6, 7, 8, 9}) int showXAxis);

    void setPageSwitchUpdateItem(@NotNull SlotPos slotPos, @NotNull PageUpdatableItem menuItem);

    void setPageSwitchUpdateItem(int row, int column, @NotNull PageUpdatableItem menuItem);

    void setPageSwitchUpdateItem(int slot, @NotNull PageUpdatableItem menuItem);


    /* SCHEDULER */
    @NotNull InventoryContentsScheduler scheduler();


    /* CACHE */
    <T> T cache(@NotNull String key, T def);

    <T> T cache(@NotNull String key);

    @NotNull
    InventoryContents setCache(@NotNull String key, @NotNull Object value);

    @NotNull
    InventoryContents pruneCache(@NotNull String key);


    /* OTHER */
    void setTitle(@NotNull String title);

    void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action);

    @Nullable
    String getSearchQuery(@NotNull String id);
}