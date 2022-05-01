package nl.tritewolf.tritemenus.scrollable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Getter
public class Scrollable {

    private final String id;
    private final InventoryContents contents;
    private final MenuIterator iterator;
    private final int showYAxis;
    private final int showXAxis;

    private final Map<Integer, Supplier<MenuItem>[]> yAxis = new HashMap<>();
    private final Map<Integer, Supplier<MenuItem>[]> xAxis = new HashMap<>();

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

    public Scrollable addItem(Supplier<MenuItem> menuItem) {
        Integer next = iterator.next();
        SlotPos slotPos = SlotPos.of(next);

        if (!iterator.hasNext() || (slotPos.getRow() - iterator.getRow()) >= showXAxis || (slotPos.getColumn() - iterator.getColumn()) >= showYAxis) {
            iterator.reset();
            slotPos = SlotPos.of(iterator.next());
        }

        Supplier<MenuItem>[] suppliersYAxis = yAxis.computeIfAbsent(yIndex, integer -> new Supplier[showXAxis]);
        Supplier<MenuItem>[] suppliersXAxis = xAxis.computeIfAbsent(xIndex, integer -> new Supplier[showYAxis]);

        if (slotPos.getColumn() <= showYAxis) {
            System.out.println(yIndex);
            suppliersYAxis[slotPos.getRow() - iterator.getRow()] = menuItem;
        }
        if (slotPos.getRow() <= showXAxis) suppliersXAxis[slotPos.getColumn() - iterator.getColumn()] = menuItem;

        if (Arrays.stream(suppliersYAxis).noneMatch(Objects::isNull)) yIndex++;
        if (Arrays.stream(suppliersXAxis).noneMatch(Objects::isNull)) xIndex++;
        return this;
    }


    public Scrollable nextYAxis() {
        openYAxis(++currentYAxis);
        return this;
    }

    public Scrollable nextXAxis() {
        openXAxis(currentXAxis++);
        return this;
    }

    public Scrollable openYAxis(int newYAxis) {
        MenuObject menuObject = contents.getTriteMenu();
        for (int y = iterator.getColumn(); y < iterator.getColumn() + showYAxis; y++) {
            Supplier<MenuItem>[] items = yAxis.get(y + newYAxis);
            for (int x = iterator.getRow(); x < items.length + iterator.getRow(); x++) {
                int slot = SlotPos.of(x, y).getSlot();
                if (items[x - iterator.getRow()] == null) {
                    InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
                    continue;
                }
                InventoryUtils.updateItem(menuObject.getPlayer(), slot, items[x - iterator.getRow()].get().getItemStack(), menuObject.getInventory());
            }
        }

        return this;
    }

    public Scrollable openXAxis(int newXAxis) {
        MenuObject menuObject = contents.getTriteMenu();

        for (int x = iterator.getRow(); x < iterator.getRow() + showXAxis; x++) {
            Supplier<MenuItem>[] items = xAxis.get(x + newXAxis);
            for (int y = iterator.getColumn(); y < items.length + iterator.getColumn(); y++) {
                int slot = SlotPos.of(x, y).getSlot();

                if (items[y - iterator.getColumn()] == null) {
                    InventoryUtils.updateItem(menuObject.getPlayer(), slot, new ItemStack(Material.AIR), menuObject.getInventory());
                    continue;
                }
                InventoryUtils.updateItem(menuObject.getPlayer(), slot, items[y - iterator.getColumn()].get().getItemStack(), menuObject.getInventory());
            }
        }

        return this;
    }

}
