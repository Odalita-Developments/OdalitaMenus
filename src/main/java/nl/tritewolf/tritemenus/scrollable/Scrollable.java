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

        this.yIndex = iterator.getColumn();
        this.xIndex = iterator.getRow();
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

        if (index <= this.showXAxis * this.showYAxis) {
            this.contents.setAsync(slotPos, menuItem.get());
        }

        if (this.iterator.getMenuIteratorType() == MenuIteratorType.HORIZONTAL && this.yIndex <= this.showYAxis) {
            ++this.yIndex;
        }

        if (this.iterator.getMenuIteratorType() == MenuIteratorType.VERTICAL && this.xIndex <= this.showXAxis) {
            ++this.xIndex;
        }

        return this;
    }


    public Scrollable nextYAxis() {
        return this.openYAxis(++this.currentYAxis);
    }

    public Scrollable previousYAxis() {
        return this.openYAxis(--this.currentYAxis);
    }

    public Scrollable nextXAxis() {
        return this.openXAxis(++this.currentXAxis);
    }

    public Scrollable previousXAxis() {
        return this.openXAxis(--this.currentXAxis);
    }

    public Scrollable openYAxis(int newYAxis) {
        MenuObject menuObject = this.contents.getTriteMenu();

        for (int y = newYAxis; y < newYAxis + this.showYAxis; y++) {
            for (int x = this.iterator.getColumn(); x < this.iterator.getColumn() + this.showXAxis; x++) {
                int index = this.getIndex(x, y);
                int slot = SlotPos.of(x, this.iterator.getRow() + (y - newYAxis)).getSlot();

                Supplier<MenuItem> menuItemSupplier = this.items.get(index);
                if (menuItemSupplier == null) {
                    InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
                    continue;
                }

                InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItemSupplier.get().getItemStack(), menuObject.getInventory());
            }
        }

        return this;
    }

    public Scrollable openXAxis(int newXAxis) {
        MenuObject menuObject = this.contents.getTriteMenu();

        for (int y = this.iterator.getRow(); y < this.iterator.getRow() + this.showYAxis; y++) {
            for (int x = newXAxis; x < newXAxis + this.showXAxis; x++) {
                int index = this.getIndex(x, y);
                int slot = SlotPos.of(this.iterator.getColumn() + (x - newXAxis), y).getSlot();

                Supplier<MenuItem> menuItemSupplier = this.items.get(index);
                if (menuItemSupplier == null) {
                    InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
                    continue;
                }

                InventoryUtils.updateItem(menuObject.getPlayer(), slot, menuItemSupplier.get().getItemStack(), menuObject.getInventory());
            }
        }

        return this;
    }

    private int getIndex(int x, int y) {
        return (y * 9) + x;
    }
}