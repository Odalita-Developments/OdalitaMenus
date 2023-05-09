package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.annotations.MenuFrame;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.*;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedFeatures;
import nl.odalitadevelopments.menus.pagination.PaginationBuilder;
import nl.odalitadevelopments.menus.patterns.*;
import nl.odalitadevelopments.menus.scrollable.ScrollableBuilder;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

sealed class MenuContentsImpl implements MenuContents permits MenuFrameContentsImpl {

    final MenuSession menuSession;
    final MenuSessionCache cache;
    final MenuContentsScheduler scheduler;
    final MenuContentsActions actions;
    final MenuContentsEvents events;

    MenuContentsImpl(MenuSession menuSession) {
        this.menuSession = menuSession;
        this.cache = menuSession.getCache();
        this.scheduler = new MenuContentsSchedulerImpl(this);
        actions = new MenuContentsActionsImpl(this);
        this.events = new MenuContentsEventsImpl(this);
    }

    MenuContentsImpl(MenuSession menuSession, MenuSessionCache cache, MenuContentsScheduler scheduler, MenuContentsActions actions, MenuContentsEvents events) {
        this.menuSession = menuSession;
        this.cache = cache;
        this.scheduler = scheduler;
        this.actions = actions;
        this.events = events;
    }

    @Override
    public @NotNull MenuSession menuSession() {
        return this.menuSession;
    }

    @Override
    public @NotNull MenuContentsScheduler scheduler() {
        return this.scheduler;
    }

    @Override
    public @NotNull MenuContentsActions actions() {
        return this.actions;
    }

    @Override
    public @NotNull MenuContentsEvents events() {
        return this.events;
    }

    @Override
    public @Nullable MenuFrameData menuFrameData() {
        return null;
    }

    @Override
    public void set(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        this.set(slotPos, item, override, (slot) -> {
            this.menuSession.contents[slot.getRow()][slot.getColumn()] = item;

            if (this.menuSession.isOpened()) {
                InventoryUtils.updateItem(this.menuSession.getPlayer(), slot.getSlot(), item.getItemStack(this.menuSession.getInstance()), this.menuSession.getInventory());
            }
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
        for (int row = 0; row < this.maxColumns(); row++) {
            for (int column = 0; column < this.maxColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);

                if (this.menuSession.getContent(slotPos) == null) {
                    return Optional.of(slotPos);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isEmpty(@NotNull SlotPos slotPos) { // TODO override
        return this.menuSession.getContent(slotPos) == null;
    }

    @Override
    public boolean isEmpty(int row, int column) { // TODO override
        return this.isEmpty(SlotPos.of(row, column));
    }

    @Override
    public boolean isEmpty(int slot) { // TODO override
        return this.isEmpty(SlotPos.of(slot));
    }

    @Override
    public void fillRow(int row, @NotNull MenuItem item) {
        if (row < 0 || row >= this.maxRows()) {
            throw new IllegalArgumentException("Row must be between '0' and '" + this.maxRows() + "'");
        }

        for (int column = 0; column < this.maxColumns(); column++) {
            this.set(row, column, item);
        }
    }

    @Override
    public void fillColumn(int column, @NotNull MenuItem item) {
        if (column < 0 || column >= this.maxColumns()) {
            throw new IllegalArgumentException("Column must be between '0' and '" + this.maxColumns() + "'");
        }

        for (int row = 0; row < this.maxRows(); row++) {
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
        for (int row = 0; row < this.maxRows(); row++) {
            for (int column = 0; column < this.maxColumns(); column++) {
                SlotPos slotPos = SlotPos.of(row, column);
                if (this.menuSession.getContent(slotPos) != null || this.cache.getPlaceableItems().contains(slotPos.getSlot())) {
                    continue;
                }

                this.set(slotPos, item);
            }
        }
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item, boolean override) {
        this.set(slotPos, item, override);
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(@NotNull SlotPos slotPos, @NotNull MenuItem item) {
        this.set(slotPos, item, true);
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(int row, int column, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(row, column), item, override);
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(int row, int column, @NotNull MenuItem item) {
        this.set(row, column, item, true);
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(int slot, @NotNull MenuItem item, boolean override) {
        this.set(SlotPos.of(slot), item, override);
    }

    @Override
    @Deprecated(forRemoval = true, since = "0.1.6")
    public void setAsync(int slot, @NotNull MenuItem item) {
        this.set(slot, item, true);
    }

    @Override
    public void setRefreshable(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull MenuItem> item, boolean override) {
        this.set(slotPos, item.get(), override);
        this.cache.getRefreshableItems().put(slotPos.getSlot(), item);
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
        int slot = slotPos.getSlot();

        Supplier<MenuItem> menuItemSupplier = this.cache.getRefreshableItems().get(slot);
        if (menuItemSupplier == null) {
            MenuItem item = this.menuSession.getContent(slotPos);
            if (item == null || !item.isUpdatable()) return;

            menuItemSupplier = () -> item;
        }

        this.set(slot, menuItemSupplier.get());
    }

    @Override
    public synchronized void refreshItem(int row, int column) {
        this.refreshItem(SlotPos.of(row, column));
    }

    @Override
    public synchronized void refreshItem(int slot) {
        this.refreshItem(SlotPos.of(slot));
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
        this.cache.getIterators().put(iterator, menuIterator);
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
        PatternContainer patternContainer = this.menuSession.getInstance().getPatternContainer();
        IteratorPattern iteratorPatternByClass = patternContainer.getPattern(clazz);

        if (iteratorPatternByClass == null) {
            throw new IllegalArgumentException("The pattern class '" + clazz.getName() + "' is not registered!");
        }

        this.createPatternIterator(iteratorPatternByClass, menuItems);
    }

    @Override
    public void createDirectionsPatternIterator(@NotNull Class<? extends DirectionPattern> clazz, @NotNull List<@NotNull MenuItem> menuItems) {
        PatternContainer patternContainer = this.menuSession.getInstance().getPatternContainer();
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
            this.cache.getPlaceableItems().add(slot);
        }
    }

    @Override
    public void setForcedPlaceableItem(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        this.setForcedPlaceableItem(slotPos.getSlot(), itemStack);
    }

    @Override
    public void setForcedPlaceableItem(int row, int column, @NotNull ItemStack itemStack) {
        this.setForcedPlaceableItem(SlotPos.of(row, column), itemStack);
    }

    @Override
    public void setForcedPlaceableItem(int slot, @NotNull ItemStack itemStack) {
        if (!this.menuSession.fits(slot)) return;

        this.menuSession.getInventory().setItem(slot, itemStack);
    }

    @Override
    public void placeableItemsCloseAction(@NotNull PlaceableItemsCloseAction action) {
        this.cache.setPlaceableItemsCloseAction(action);
    }

    @Override
    public @NotNull Optional<@NotNull SlotPos> firstEmptyPlaceableItemSlot() {
        MenuSessionCache cache = this.cache;
        if (cache.getPlaceableItems().isEmpty()) return Optional.empty();

        for (int slot : cache.getPlaceableItems()) {
            ItemStack item = this.menuSession.getInventory().getItem(slot);
            if (item == null || item.getType().isAir()) {
                return Optional.of(SlotPos.of(slot));
            }
        }

        return Optional.empty();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getPlaceableItems() {
        Map<Integer, ItemStack> items = new HashMap<>();
        MenuSessionCache cache = this.cache;
        if (cache.getPlaceableItems().isEmpty()) return items;

        for (int slot : cache.getPlaceableItems()) {
            ItemStack item = this.menuSession.getInventory().getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            items.put(slot, item);
        }

        return items;
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
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, @NotNull SlotPos slotPos, @NotNull Class<F> frameClass) {
        this.registerFrame(id, slotPos.getRow(), slotPos.getColumn(), frameClass);
    }

    @Override
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int row, int column, @NotNull Class<F> frameClass) {
        if (!frameClass.isAnnotationPresent(MenuFrame.class)) {
            throw new IllegalArgumentException("The frameClass class '" + frameClass.getName() + "' is not annotated with @MenuFrame!");
        }

        MenuFrame frameData = frameClass.getAnnotation(MenuFrame.class);

        MenuFrameData previousValue = this.menuSession.getCache().getFrames().putIfAbsent(id, new MenuFrameData(id, frameData.height(), frameData.width(), row, column, frameClass));
        if (previousValue != null) {
            throw new IllegalArgumentException("The frameClass with the id '" + id + "' is already registered!");
        }
    }

    @Override
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int slot, @NotNull Class<F> frameClass) {
        this.registerFrame(id, SlotPos.of(slot), frameClass);
    }

    @Override
    public boolean loadFrame(@NotNull String id, Object @NotNull ... arguments) {
        MenuFrameData frameData = this.menuSession.getCache().getFrames().get(id);
        if (frameData == null) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not registered!");
        }

        Cooldown cooldown = this.menuSession.getInstance().getProvidersContainer().getCooldownProvider().frameLoadCooldown();
        if (cooldown != null && this.menuSession.getInstance().getCooldownContainer().checkAndCreate(this.menuSession.getPlayer().getUniqueId(), "INTERNAL_FRAME_LOAD_COOLDOWN", cooldown)) {
            return false;
        }

        if (this.menuSession.getCache().getLoadedFrameId() != null) {
            this.unloadFrame(this.menuSession.getCache().getLoadedFrameId());
        }

        Class<? extends MenuFrameProvider> frameClass = frameData.frameClass();
        MenuFrameProvider frame;
        try {
            Constructor<?> constructor;
            if (arguments.length == 0) {
                constructor = frameClass.getDeclaredConstructor();
            } else {
                Class<?>[] parameterTypes = Arrays.stream(arguments)
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);

                constructor = frameClass.getDeclaredConstructor(parameterTypes);
            }

            constructor.setAccessible(true);
            frame = (MenuFrameProvider) constructor.newInstance(arguments);
        } catch (Exception exception) {
            throw new IllegalArgumentException("The frameClass '" + frameClass.getName() + "' does not have a constructor with the arguments: " + Arrays.toString(arguments), exception);
        }

        MenuFrameProviderLoader<MenuFrameProvider> loader = this.menuSession.getInstance().getMenuProcessor().getMenuFrameProcessor().getFrameProviderLoader(frame);
        MenuFrameContentsImpl frameContents = new MenuFrameContentsImpl(this.menuSession, new MenuSessionCache(this.menuSession), frameData, this.scheduler, this.actions, this.events);
        loader.load(frame, this.menuSession.getPlayer(), frameContents);

        this.menuSession.getCache().setLoadedFrameId(id);
        return true;
    }

    @Override
    public void unloadFrame(@NotNull String id) {
        MenuFrameData frameData = this.menuSession.getCache().getFrames().get(id);
        if (frameData == null) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not registered!");
        }

        if (!id.equals(this.menuSession.getCache().getLoadedFrameId())) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not loaded!");
        }

        for (int row = frameData.startRow(); row < Math.max(6, frameData.startRow() + frameData.height()); row++) {
            for (int column = frameData.startColumn(); column < Math.max(9, frameData.startColumn() + frameData.width()); column++) {
                int slot = SlotPos.of(row, column).getSlot();
                if (this.menuSession.getCache().getFrameOverlaySlots().contains(slot)) continue;

                this.menuSession.contents[row][column] = null;
                InventoryUtils.updateItem(this.menuSession.getPlayer(), slot, null, this.menuSession.getInventory());
            }
        }

        this.menuSession.getCache().setLoadedFrameId(null);
    }

    @Override
    public void registerFrameOverlaySlots(SlotPos @NotNull ... slots) {
        for (SlotPos slot : slots) {
            this.registerFrameOverlaySlots(slot.getSlot());
        }
    }

    @Override
    public void registerFrameOverlaySlots(int... slots) {
        for (int slot : slots) {
            this.menuSession.getCache().getFrameOverlaySlots().add(slot);
        }
    }

    @Override
    public @Nullable String loadedFrameId() {
        return this.cache.getLoadedFrameId();
    }

    @Override
    public @NotNull MenuSessionCache cache() {
        return this.cache;
    }

    @Override
    public synchronized <T> T cache(@NotNull String key, T def) {
        return this.cache.cache(key, def);
    }

    @Override
    public synchronized <T> T cache(@NotNull String key) {
        return this.cache.cache(key);
    }

    @Override
    public synchronized @NotNull MenuContents setCache(@NotNull String key, @NotNull Object value) {
        this.cache.setCache(key, value);
        return this;
    }

    @Override
    public synchronized @NotNull MenuContents pruneCache(@NotNull String key) {
        this.cache.pruneCache(key);
        return this;
    }

    @Override
    public synchronized void setGlobalCacheKey(@NotNull String key) {
        this.menuSession.setGlobalCacheKey(key);
    }

    @Override
    public synchronized void setTitle(@NotNull String title) {
        this.menuSession.setTitle(title);
    }

    @Override
    public void setMenuType(@NotNull MenuType menuType) {
        this.menuSession.setMenuType(menuType);
    }

    @Override
    public void closeInventory(@NotNull Player player, @NotNull PlaceableItemsCloseAction action) {
        this.cache.setPlaceableItemsCloseAction(action);
        player.closeInventory();
    }

    protected void set(SlotPos slotPos, int originalSlot, MenuItem item, boolean override, Consumer<SlotPos> setter) {
        slotPos = this.convertSlotPos(slotPos);
        int slot = slotPos.getSlot();

        if (!this.menuSession.fits(slot)) {
            throw new IllegalArgumentException("The slot '" + slot + "' is out of bounds for this menu");
        }

        if (!override && this.menuSession.getContent(slotPos) != null) return;

        if (item instanceof PageUpdatableItem) {
            this.cache.getPageSwitchUpdateItems().put(originalSlot, item);
        }

        if (!this.menuSession.isHasUpdatableItems() && item.isUpdatable()) {
            this.menuSession.setHasUpdatableItems(true);
        }

        setter.accept(slotPos);
    }

    private void set(SlotPos slotPos, MenuItem item, boolean override, Consumer<SlotPos> setter) {
        this.set(slotPos, slotPos.getSlot(), item, override, setter);
    }

    protected SlotPos convertSlotPos(SlotPos slotPos) {
        return slotPos.convertTo(
                this.maxRows(),
                this.maxColumns()
        );
    }

    @Override
    public int maxRows() {
        return this.menuSession.getRows();
    }

    @Override
    public int maxColumns() {
        return this.menuSession.getColumns();
    }
}