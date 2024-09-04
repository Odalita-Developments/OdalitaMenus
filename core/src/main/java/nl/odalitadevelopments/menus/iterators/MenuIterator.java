package nl.odalitadevelopments.menus.iterators;

import nl.odalitadevelopments.menus.contents.frame.MenuFrameData;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import org.jetbrains.annotations.NotNull;

public final class MenuIterator extends AbstractMenuIterator<MenuIterator> {

    public MenuIterator(AbstractMenuSession<?, ?, ?> menuSession, MenuFrameData frameData, MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        super(menuSession, frameData, menuIteratorType, startRow, startColumn);
    }

    @Override
    protected @NotNull MenuIterator self() {
        return this;
    }

    public @NotNull MenuIterator set(MenuItem menuItem) {
        this.contents.set(super.getSlot(), menuItem);
        return this;
    }

    public @NotNull MenuIterator setNext(MenuItem menuItem) {
        this.contents.set(super.next(), menuItem);
        return this;
    }

    public @NotNull MenuIterator setPrevious(MenuItem menuItem) {
        this.contents.set(super.previous(), menuItem);
        return this;
    }
}
