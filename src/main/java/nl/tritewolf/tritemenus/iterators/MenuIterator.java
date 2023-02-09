package nl.tritewolf.tritemenus.iterators;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.pos.SlotPos;
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

    private InventoryContents contents;
    private boolean override = false;

    private MenuIteratorType menuIteratorType;
    private final int row;
    private final int column;

    public MenuIterator(MenuIteratorType menuIteratorType, InventoryContents contents, int startRow, int startColumn) {
        this.menuIteratorType = menuIteratorType;
        this.contents = contents;
        this.row = startRow;
        this.column = startColumn;
    }

    public MenuIterator addReusableSlot(int slot) {
        this.canStillUse.add(this.getCorrectSlotPos(slot).getSlot());
        return this;
    }

    public MenuIterator set(MenuItem menuItem) {
        this.contents.set(getSlot(), menuItem);
        return this;
    }

    public MenuIterator setNext(MenuItem menuItem) {
        this.contents.set(next(), menuItem);
        return this;
    }

    public synchronized MenuIterator setNextAsync(MenuItem menuItem) {
        this.contents.setAsync(next(), menuItem);
        return this;
    }

    public MenuIterator setPrevious(MenuItem menuItem) {
        this.contents.set(previous(), menuItem);
        return this;
    }

    public synchronized MenuIterator setPreviousAsync(MenuItem menuItem) {
        this.contents.setAsync(SlotPos.of(previous()), menuItem);
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

    public SlotPos getStartPos() {
        return this.getSlotPos(this.row, this.column);
    }

    private Inventory getInventory() {
        return this.contents.menuSession().getInventory();
    }

    private void init(@NotNull MenuIteratorType menuIteratorType) {
        switch (menuIteratorType) {
            case HORIZONTAL -> this.horizontalInitialization();
            case VERTICAL -> this.verticalInitialization();
        }
    }

    private void horizontalInitialization() {
        int inventorySize = this.contents.maxRows() * this.contents.maxColumns();
        Inventory inventory = getInventory();

        int slotStart = this.getSlotPos(this.row, this.column).getSlot();
        for (int slot = slotStart; slot < inventorySize; slot++) {
            if (blacklist.contains(slot)) continue;

            // Check if item can be overridden in inventory
            int inventorySlot = this.getCorrectSlotPos(slot).getSlot();
            ItemStack item = inventory.getItem(inventorySlot);
            if (!isOverride() && item != null && item.getType() != Material.AIR) continue;

            // Add slot to items, inventorySlot will give issues with frames
            items.add(slot);
        }
    }

    private void verticalInitialization() {
        Inventory inventory = getInventory();

        for (int column = this.column; column < this.contents.maxColumns(); column++) {
            for (int row = this.row; row < this.contents.maxRows(); row++) {
                int currentSlot = this.getSlotPos(row, column).getSlot();
                if (blacklist.contains(currentSlot)) continue;

                currentSlot = this.getCorrectSlotPos(currentSlot).getSlot();

                ItemStack item = inventory.getItem(currentSlot);
                if (!isOverride() && item != null && item.getType() != Material.AIR) continue;

                items.add(currentSlot);
            }
        }
    }

    private SlotPos getSlotPos(int row, int column) {
        return SlotPos.of(
                this.contents.maxRows(),
                this.contents.maxColumns(),
                row,
                column
        );
    }

    private SlotPos getSlotPos(int slot) {
        return SlotPos.of(
                this.contents.maxRows(),
                this.contents.maxColumns(),
                slot
        );
    }

    private SlotPos getCorrectSlotPos(int slot) {
        return this.getSlotPos(slot).convertFromFrame(
                this.contents.menuSession().getRows(),
                this.contents.menuSession().getColumns(),
                this.contents.menuFrameData()
        );
    }
}
