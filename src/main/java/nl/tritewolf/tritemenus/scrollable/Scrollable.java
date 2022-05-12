package nl.tritewolf.tritemenus.scrollable;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
public class Scrollable {

    private final String id;
    private final InventoryContents contents;
    private final MenuIterator iterator;
    private final int showYAxis;
    private final int showXAxis;

    private final Map<Integer, Supplier<MenuItem>> items = new HashMap<>();

    @Setter
    private int currentYAxis = 0;
    @Setter
    private int currentXAxis = 0;

    private int yIndex;
    private int xIndex;

    public Scrollable(String id, InventoryContents contents, MenuIterator iterator, int showYAxis, int showXAxis) {
        this.id = id;
        this.contents = contents;
        this.iterator = iterator;
        this.showYAxis = showYAxis;
        this.showXAxis = showXAxis;

        this.yIndex = iterator.getRow();
        this.xIndex = iterator.getColumn();
    }

    public synchronized Scrollable addItem(Supplier<MenuItem> menuItem) {
        int next = this.iterator.next();
        SlotPos slotPos = SlotPos.of(next);

        if (!this.iterator.hasNext() || (slotPos.getRow() - this.iterator.getRow()) >= this.showXAxis || (slotPos.getColumn() - this.iterator.getColumn()) >= this.showYAxis) {
            this.iterator.reset();
            slotPos = SlotPos.of(this.iterator.next());
        }

        int index = this.getIndex(this.xIndex, this.yIndex);
        this.items.put(index, menuItem);

        SlotPos slot = SlotPos.of(this.xIndex, this.yIndex);

        if (slot.getSlot() <= Math.max(this.iterator.getColumn() + this.showXAxis, 8) * Math.max(this.iterator.getRow() + this.showYAxis, 5)) {
            this.contents.setAsync(slot, menuItem.get());
        }

        if (this.iterator.getMenuIteratorType() == MenuIteratorType.HORIZONTAL) {
            if (this.xIndex >= this.showXAxis) {
                ++this.yIndex;
                this.xIndex = this.iterator.getColumn();

                return this;
            }

            ++this.xIndex;
        }

        if (this.iterator.getMenuIteratorType() == MenuIteratorType.VERTICAL) {
            if (this.yIndex <= this.showYAxis) {
                ++this.xIndex;
                this.yIndex = this.iterator.getRow();

                return this;
            }

            ++this.yIndex;
        }

        return this;
    }

    public int lastYAxis() {
        return (int) Math.ceil((double) this.items.size() / (double) this.showXAxis);
    }

    public int lastXAxis() {
        return (int) Math.ceil((double) this.items.size() / (double) this.showYAxis);
    }

    public Scrollable nextYAxis() {
        return this.openYAxis(this.currentYAxis + 1);
    }

    public Scrollable previousYAxis() {
        return this.openYAxis(this.currentYAxis - 1);
    }

    public Scrollable nextXAxis() {
        return this.openXAxis(this.currentXAxis + 1);
    }

    public Scrollable previousXAxis() {
        return this.openXAxis(this.currentXAxis - 1);
    }

    public Scrollable openYAxis(int newYAxis) {
        newYAxis = Math.max(0, Math.min(newYAxis, this.lastYAxis()));

        MenuObject menuObject = this.contents.getTriteMenu();

        for (int y = newYAxis; y < newYAxis + this.showYAxis; y++) {
            for (int x = this.iterator.getColumn(); x < this.iterator.getColumn() + this.showXAxis; x++) {
                int index = this.getIndex(x, y);
                int slot = SlotPos.of(this.iterator.getRow() + (y - newYAxis), x).getSlot();

                this.updateItem(menuObject, slot, index);
            }
        }

        this.currentYAxis = newYAxis;

        return this;
    }

    public Scrollable openXAxis(int newXAxis) {
        newXAxis = Math.max(0, Math.min(newXAxis, this.lastXAxis()));

        MenuObject menuObject = this.contents.getTriteMenu();

        for (int y = this.iterator.getRow(); y < this.iterator.getRow() + this.showYAxis; y++) {
            for (int x = newXAxis; x < newXAxis + this.showXAxis; x++) {
                int index = this.getIndex(x, y);
                int slot = SlotPos.of(y, this.iterator.getColumn() + (x - newXAxis)).getSlot();

                this.updateItem(menuObject, slot, index);
            }
        }

        this.currentXAxis = newXAxis;

        return this;
    }

    private void updateItem(MenuObject menuObject, int slot, int index) {
        Supplier<MenuItem> menuItemSupplier = this.items.get(index);
        if (menuItemSupplier == null) {
            InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
            return;
        }

        InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItemSupplier.get().getItemStack(), menuObject.getInventory());
    }

    private int getIndex(int x, int y) {
        return (y * 9) + x;
    }
}