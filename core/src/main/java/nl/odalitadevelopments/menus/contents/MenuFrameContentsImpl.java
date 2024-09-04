package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.frame.MenuFrameData;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.patterns.MenuPattern;
import nl.odalitadevelopments.menus.patterns.PatternCache;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

final class MenuFrameContentsImpl implements MenuFrameContents, IMenuContentsImpl, IPaginationScrollableContentsImpl {

    private final AbstractMenuSession<?, ?, ?> menuSession;
    private final MenuSessionCache cache;

    private final MenuContentsScheduler scheduler;

    private final MenuFrameData frameData;

    MenuFrameContentsImpl(AbstractMenuSession<?, ?, ?> menuSession, MenuFrameData frameData) {
        this.menuSession = menuSession;
        this.cache = new MenuSessionCache(menuSession.instance(), menuSession.data());

        this.scheduler = MenuContentsScheduler.create(this.cache);

        this.frameData = frameData;
    }

    @Override
    public AbstractMenuSession<?, ?, ?> menuSession() {
        return this.menuSession;
    }

    @Override
    public @NotNull MenuContentsScheduler scheduler() {
        return this.scheduler;
    }

    @Override
    public @NotNull MenuSessionCache cache() {
        return this.cache;
    }

    @Override
    public @NotNull MenuFrameData menuFrameData() {
        return this.frameData;
    }

    @Override
    public boolean isEmpty(@NotNull SlotPos slotPos) {
        slotPos = slotPos.convertTo(this.maxRows(), this.maxColumns());
        slotPos = slotPos.convertFromFrame(this.menuSession.rows(), this.menuSession.columns(), this.frameData);
        return this.menuSession.content(slotPos) == null;
    }

    @Override
    public boolean isEmpty(int row, int column) {
        return this.isEmpty(SlotPos.of(
                this.maxRows(),
                this.maxColumns(),
                row,
                column
        ));
    }

    @Override
    public boolean isEmpty(int slot) {
        return this.isEmpty(SlotPos.of(
                this.maxRows(),
                this.maxColumns(),
                slot
        ));
    }

    @Override
    public @NotNull MenuIterator createIterator(@NotNull String iterator, @NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        MenuIterator menuIterator = new MenuIterator(this.menuSession, this.frameData, menuIteratorType, startRow, startColumn);
        this.cache.getIterators().put(iterator, menuIterator);
        return menuIterator;
    }

    @Override
    public void createSimpleIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn, @NotNull List<@NotNull MenuItem> menuItems,
                                     int... blacklisted) {
        MenuIterator menuIterator = new MenuIterator(this.menuSession, this.frameData, menuIteratorType, startRow, startColumn);
        menuIterator.blacklist(blacklisted);

        for (MenuItem menuItem : menuItems) {
            menuIterator.setNext(menuItem);
        }
    }

    @Override
    public @NotNull <T> MenuObjectIterator<T> createObjectIterator(@NotNull MenuIteratorType menuIteratorType, int startRow, int startColumn,
                                                                   @NotNull Class<T> clazz,
                                                                   @NotNull Function<@NotNull T, @NotNull MenuItem> menuItemCreatorFunction) {
        return new MenuObjectIterator<>(this.menuSession, this.frameData, menuIteratorType, startRow, startColumn, menuItemCreatorFunction);
    }

    @Override
    public <C extends PatternCache<T>, T> void createPatternIterator(@NotNull MenuPattern<C> iteratorPattern, @NotNull List<@NotNull MenuItem> menuItems) {
        MenuIterator value = new MenuIterator(this.menuSession, this.frameData, MenuIteratorType.PATTERN, 0, 0);
        iteratorPattern.handle(value);

        for (MenuItem menuItem : menuItems) {
            value.setNext(menuItem);
        }
    }

    @Override
    public SlotPos calculateSlotPos(SlotPos slotPos) {
        slotPos = slotPos.convertTo(this.maxRows(), this.maxColumns());
        return slotPos.convertFromFrame(this.menuSession.rows(), this.menuSession.columns(), this.frameData);
    }

    @Override
    public int maxRows() {
        return this.frameData.height();
    }

    @Override
    public int maxColumns() {
        return this.frameData.width();
    }
}