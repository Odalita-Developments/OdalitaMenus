package nl.odalitadevelopments.menus.iterators;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class MenuObjectIterator<T> extends AbstractMenuIterator<MenuObjectIterator<T>> {

    private MenuObjectIteratorSet<T> objects;
    private Comparator<T> comparator;
    private final Function<T, MenuItem> menuItemCreatorFunction;

    private final Map<T, Integer> slotPositions = new HashMap<>();

    public MenuObjectIterator(MenuContents contents, MenuIteratorType type, int startRow, int startColumn,
                              Collection<T> objects, Function<T, MenuItem> menuItemCreatorFunction) {
        super(contents, type, startRow, startColumn);

        this.objects = new MenuObjectIteratorSet<>(objects);
        this.menuItemCreatorFunction = menuItemCreatorFunction;
    }

    @Override
    protected @NotNull MenuObjectIterator<T> getInstance() {
        return this;
    }

    public void sort(@NotNull Comparator<@NotNull T> comparator) {
        this.reset();

        this.objects = this.objects.sort(comparator);

        int index = 0;
        for (T value : this.objects) {
            int currentSlotPosition = this.slotPositions.get(value);
            MenuItem item = this.contents.menuSession().getContent(this.getSlotPos(currentSlotPosition));

//            this.setNext(value, item);
            index++;
        }

        for (int i = index; i < this.availableSlotPositions.size(); i++) {
            this.clearItem(this.availableSlotPositions.get(i));
        }
    }

    private @NotNull MenuObjectIterator<T> set0(@NotNull T value, @Nullable MenuItem item, int slot) {
        if (item == null) item = this.menuItemCreatorFunction.apply(value);

        this.contents.set(slot, item);
        this.slotPositions.put(value, slot);
        return this;
    }

    private void clearItem(int slot) {
        this.contents.set(slot, DisplayItem.of(new ItemStack(Material.AIR)));
    }

    private static final class MenuObjectIteratorSet<T> extends TreeSet<T> {

        public MenuObjectIteratorSet(@NotNull Collection<? extends T> c) {
            super(c);
        }

        private MenuObjectIteratorSet() {
        }

        private MenuObjectIteratorSet(Comparator<? super T> comparator) {
            super(comparator);
        }

        private MenuObjectIteratorSet(SortedSet<T> s) {
            super(s);
        }

        public @NotNull MenuObjectIteratorSet<T> sort(@NotNull Comparator<T> comparator) {
            MenuObjectIteratorSet<T> newList = new MenuObjectIteratorSet<>(comparator);
            newList.addAll(this);
            return newList;
        }
    }
}