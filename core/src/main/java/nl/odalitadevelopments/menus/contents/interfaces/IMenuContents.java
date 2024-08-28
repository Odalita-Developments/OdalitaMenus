package nl.odalitadevelopments.menus.contents.interfaces;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.patterns.DirectionPattern;
import nl.odalitadevelopments.menus.patterns.IteratorPattern;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternCache;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IMenuContents {

    @NotNull
    MenuSession menuSession();

    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override);

    void set(@NotNull SlotPos slotPos, @NotNull MenuItem item);

    void set(int row, int column, @NotNull MenuItem item, boolean override);

    void set(int row, int column, @NotNull MenuItem item);

    void set(int slot, @NotNull MenuItem item, boolean override);

    void set(int slot, @NotNull MenuItem item);

    void add(@NotNull MenuItem item);

    @NotNull Optional<@NotNull SlotPos> firstEmptySlot();

    void clear(@NotNull SlotPos slotPos);

    void clear(int row, int column);

    void clear(int slot);

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

    void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack, int time);

    void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack, int time);

    void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack, int time);

    void setUpdatable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull ItemStack> itemStack);

    void setUpdatable(int row, int column, @NotNull Supplier<@NotNull ItemStack> itemStack);

    void setUpdatable(int slot, @NotNull Supplier<@NotNull ItemStack> itemStack);


    /* ITERATORS */
    @NotNull
    MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn);

    void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                              @NotNull List<@NotNull MenuItem> menuItems, int... blacklisted);

    @NotNull <T> MenuObjectIterator<T> createObjectIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                                                            @NotNull Class<T> clazz,
                                                            @NotNull Function<@NotNull T, @NotNull MenuItem> menuItemCreatorFunction);

    <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems);

    void createPatternIterator(@NotNull Class<? extends IteratorPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems);

    void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems);


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
    void setId(@NotNull String id);

    void setTitle(@NotNull String title);

    void setMenuType(@NotNull MenuType menuType);

    int maxRows();

    int maxColumns();
}