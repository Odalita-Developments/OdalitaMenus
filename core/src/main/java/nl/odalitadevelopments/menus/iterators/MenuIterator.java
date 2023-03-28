package nl.odalitadevelopments.menus.iterators;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.jetbrains.annotations.NotNull;

public final class MenuIterator extends AbstractMenuIterator<MenuIterator> {

    public MenuIterator(MenuContents contents, MenuIteratorType menuIteratorType, int startRow, int startColumn) {
        super(contents, menuIteratorType, startRow, startColumn);
    }

    @Override
    protected @NotNull MenuIterator getInstance() {
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

    public synchronized @NotNull MenuIterator setNextAsync(MenuItem menuItem) {
        this.contents.set(super.next(), menuItem);
        return this;
    }

    public @NotNull MenuIterator setPrevious(MenuItem menuItem) {
        this.contents.set(super.previous(), menuItem);
        return this;
    }

    public synchronized @NotNull MenuIterator setPreviousAsync(MenuItem menuItem) {
        this.contents.set(super.previous(), menuItem);
        return this;
    }
}
