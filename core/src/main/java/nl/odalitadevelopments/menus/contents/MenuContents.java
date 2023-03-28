package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.pagination.PaginationBuilder;
import nl.odalitadevelopments.menus.patterns.DirectionPattern;
import nl.odalitadevelopments.menus.patterns.IteratorPattern;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternCache;
import nl.odalitadevelopments.menus.scrollable.ScrollableBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public sealed interface MenuContents permits MenuContentsImpl {

    @ApiStatus.Internal
    static MenuContents create(@NotNull MenuSession menuSession) {
        return new MenuContentsImpl(menuSession);
    }

    @NotNull
    MenuSession menuSession();

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsEvents events();

    @Nullable
    MenuFrameData menuFrameData();


    /* DEFAULT */
    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override);

    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item);

    void set(int row, int column, @NotNull MenuItem item, boolean override);

    void set(int row, int column, @NotNull MenuItem item);

    void set(int slot, @NotNull MenuItem item, boolean override);

    void set(int slot, @NotNull MenuItem item);

    void add(@NotNull MenuItem item);

    @NotNull Optional<@NotNull SlotPos> firstEmptySlot();

    boolean isEmpty(@NotNull SlotPos slotPos);

    boolean isEmpty(int row, int column);

    boolean isEmpty(int slot);

    void fillRow(int row, @NotNull MenuItem item);

    void fillColumn(int column, @NotNull MenuItem item);

    void fillRectangle(@NotNull SlotPos fromPos, @NotNull SlotPos toPos, @NotNull MenuItem item);

    void fillRectangle(int fromRow, int fromColumn, int toRow, int toColumn, @NotNull MenuItem item);

    void fillRectangle(int fromSlot, int toSlot, @NotNull MenuItem item);

    void fillBorders(@NotNull MenuItem item);

    void fill(@NotNull MenuItem item);


    /* DEFAULT ASYNC */
    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override);

    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item);

    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(int row, int column, @NotNull MenuItem item, boolean override);

    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(int row, int column, @NotNull MenuItem item);

    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(int slot, @NotNull MenuItem item, boolean override);

    @Deprecated(forRemoval = true, since = "0.1.6")
    void setAsync(int slot, @NotNull MenuItem item);


    /* DEFAULT REFRESHABLE */
    void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item, boolean override);

    void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item);

    void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item, boolean override);

    void setRefreshable(int row, int column, @NotNull Supplier<@NotNull MenuItem> item);

    void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item, boolean override);

    void setRefreshable(int slot, @NotNull Supplier<@NotNull MenuItem> item);

    void refreshItem(@NotNull SlotPos slotPos);

    void refreshItem(int row, int column);

    void refreshItem(int slot);

    void refreshItems(SlotPos @NotNull ... slotPosses);

    void refreshItems(int @NotNull ... slots);


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

    void setForcedPlaceableItem(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack);

    void setForcedPlaceableItem(int row, int column, @NotNull ItemStack itemStack);

    void setForcedPlaceableItem(int slot, @NotNull ItemStack itemStack);

    void placeableItemsCloseAction(@NotNull PlaceableItemsCloseAction action);

    @NotNull Optional<@NotNull SlotPos> firstEmptyPlaceableItemSlot();

    @NotNull Map<Integer, ItemStack> getPlaceableItems();


    /* PAGINATION & SCROLLABLE */
    @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator);

    @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage);

    @NotNull ScrollableBuilder scrollable(@NotNull String id, int showYAxis, int showXAxis);

    void setPageSwitchUpdateItem(@NotNull SlotPos slotPos, @NotNull PageUpdatableItem menuItem);

    void setPageSwitchUpdateItem(int row, int column, @NotNull PageUpdatableItem menuItem);

    void setPageSwitchUpdateItem(int slot, @NotNull PageUpdatableItem menuItem);


    /* FRAME */
    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, @NotNull SlotPos slotPos, @NotNull Class<F> frame);

    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int row, int column, @NotNull Class<F> frame);

    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int slot, @NotNull Class<F> frame);

    boolean loadFrame(@NotNull String id, Object @NotNull ... arguments);

    void unloadFrame(@NotNull String id);

    void registerFrameOverlaySlots(SlotPos @NotNull ... slots);

    void registerFrameOverlaySlots(int... slots);

    @Nullable String loadedFrameId();


    /* CACHE */
    @NotNull
    MenuSessionCache cache();

    <T> T cache(@NotNull String key, T def);

    <T> T cache(@NotNull String key);

    @NotNull
    MenuContents setCache(@NotNull String key, @NotNull Object value);

    @NotNull
    MenuContents pruneCache(@NotNull String key);

    void setGlobalCacheKey(@NotNull String key);

    /* OTHER */
    void setTitle(@NotNull String title);

    void setMenuType(@NotNull MenuType menuType);

    void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action);

    int maxRows();

    int maxColumns();
}