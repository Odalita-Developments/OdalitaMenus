package nl.tritewolf.tritemenus.iterators;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
public class MenuIterator {

    private final List<Integer> canStillUse = new ArrayList<>();
    private final List<Integer> items = new ArrayList<>();
    private final Set<Integer> blacklist = new HashSet<>();

    private int index = 0;

    private InventoryContents inventoryContents;
    private boolean override = false;

    private MenuIteratorType menuIteratorType;
    private final int row;
    private final int column;

    public MenuIterator(MenuIteratorType menuIteratorType, InventoryContents inventoryContents, int startRow, int startColumn) {
        this.menuIteratorType = menuIteratorType;
        this.inventoryContents = inventoryContents;
        this.row = startRow;
        this.column = startColumn;
    }

    public void init(@NotNull MenuIteratorType menuIteratorType) {
        switch (menuIteratorType) {
            case HORIZONTAL:
                horizontalInitialization();
                break;
            case VERTICAL:
                verticalInitialization();
                break;
        }
    }

    public MenuIterator addReusableSlot(int slot) {
        this.canStillUse.add(slot);
        return this;
    }

    public MenuIterator set(MenuItem menuItem) {
        this.inventoryContents.set(getSlot(), menuItem);
        return this;
    }

    public MenuIterator setNext(MenuItem menuItem) {
        this.inventoryContents.set(next(), menuItem);
        return this;
    }

    public synchronized MenuIterator setNextAsync(MenuItem menuItem) {
        this.inventoryContents.setAsync(SlotPos.of(next()), menuItem);
        return this;
    }

    public MenuIterator setPrevious(MenuItem menuItem) {
        this.inventoryContents.set(previous(), menuItem);
        return this;
    }

    public synchronized MenuIterator setPreviousAsync(MenuItem menuItem) {
        this.inventoryContents.setAsync(SlotPos.of(previous()), menuItem);
        return this;
    }

    public MenuIterator setOverride(Boolean override) {
        this.override = override;
        return this;
    }

    public boolean hasNext() {
        if (items.isEmpty()) init(this.menuIteratorType);

        Iterator<Integer> iterator = canStillUse.iterator();
        if (iterator.hasNext()) return true;
        return items.size() > (index + 1);
    }

    public Integer getSlot() {
        if (items.isEmpty()) init(this.menuIteratorType);

        Iterator<Integer> iterator = canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return items.get(index);
    }

    public Integer next() {
        if (items.isEmpty()) init(this.menuIteratorType);

        Iterator<Integer> iterator = canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return items.get(index++);
    }

    public Integer previous() {
        if (items.isEmpty()) init(this.menuIteratorType);
        if (index == 0) return 0;

        Iterator<Integer> iterator = canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return items.get(--index);
    }

    public MenuIterator reset() {
        index = 0;
        canStillUse.clear();
        return this;
    }

    public MenuIterator blacklist(int slot) {
        this.blacklist.add(slot);
        return this;
    }

    public MenuIterator blacklist(int... slots) {
        Arrays.stream(slots).forEach(this.blacklist::add);
        return this;
    }

    private Inventory getInventory() {
        return this.inventoryContents.getTriteMenu().getInventory();
    }

    private void horizontalInitialization() {
        int inventorySize = this.inventoryContents.getTriteMenu().getRows() * 9;
        Inventory inventory = getInventory();

        int slotStart = SlotPos.of(this.row, this.column).getSlot();
        for (int slot = slotStart; slot < inventorySize; slot++) {
            if (blacklist.contains(slot)) continue;

            ItemStack item = inventory.getItem(slot);
            if (!isOverride() && item != null && item.getType() != Material.AIR) continue;

            items.add(slot);
        }
    }

    private void verticalInitialization() {
        Inventory inventory = getInventory();

        for (int column = this.column; column < 9; column++) {
            for (int row = this.row; row < this.inventoryContents.getTriteMenu().getRows(); row++) {
                int currentSlot = SlotPos.of(row, column).getSlot();
                if (blacklist.contains(currentSlot)) continue;


                ItemStack item = inventory.getItem(currentSlot);
                if (!isOverride() && item != null && item.getType() != Material.AIR) continue;

                items.add(currentSlot);
            }
        }
    }
}