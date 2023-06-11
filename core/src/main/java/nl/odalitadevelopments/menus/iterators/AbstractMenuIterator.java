package nl.odalitadevelopments.menus.iterators;

import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public sealed abstract class AbstractMenuIterator<T extends AbstractMenuIterator<T>> permits MenuIterator, MenuObjectIterator {

    private final T instance;
    protected final MenuContents contents;

    protected final MenuIteratorType type;
    protected final int startRow;
    protected final int startColumn;

    protected final List<Integer> availableSlotPositions = new ArrayList<>();
    protected final List<Integer> canStillUse = new ArrayList<>();
    protected final Set<Integer> blacklist = new HashSet<>();

    protected int index = 0;
    protected boolean override = false;

    protected AbstractMenuIterator(MenuContents contents, MenuIteratorType type, int startRow, int startColumn) {
        this.instance = this.self();

        this.contents = contents;
        this.type = type;
        this.startRow = startRow;
        this.startColumn = startColumn;
    }

    protected abstract @NotNull T self();

    public boolean hasNext() {
        this.init(this.type);

        Iterator<Integer> iterator = this.canStillUse.iterator();
        if (iterator.hasNext()) return true;
        return this.availableSlotPositions.size() > (this.index + 1);
    }

    public int getSlot() {
        this.init(this.type);

        Iterator<Integer> iterator = this.canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return this.availableSlotPositions.get(this.index);
    }

    public int next() {
        this.init(this.type);

        Iterator<Integer> iterator = this.canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return this.availableSlotPositions.get(this.index++);
    }

    public int previous() {
        this.init(this.type);

        Iterator<Integer> iterator = this.canStillUse.iterator();
        if (iterator.hasNext()) {
            Integer slot = iterator.next();
            iterator.remove();
            return slot;
        }

        return this.availableSlotPositions.get(--this.index);
    }

    public @NotNull T addReusableSlot(int slot) {
        this.canStillUse.add(this.getCorrectSlotPos(slot).getSlot());
        return this.instance;
    }

    public @NotNull T reset() {
        this.index = 0;
        this.canStillUse.clear();
        return this.instance;
    }

    public @NotNull T blacklist(int slot) {
        this.blacklist.add(slot);
        return this.instance;
    }

    public @NotNull T blacklist(int... slots) {
        Arrays.stream(slots).forEach(this.blacklist::add);
        return this.instance;
    }

    public @NotNull T override(boolean override) {
        this.override = override;
        return this.instance;
    }

    public @NotNull SlotPos getStartSlotPos() {
        return this.getSlotPos(
                this.startRow,
                this.startColumn
        );
    }

    protected @NotNull Inventory getInventory() {
        return this.contents.menuSession().getInventory();
    }

    protected @NotNull SlotPos getSlotPos(int row, int column) {
        return SlotPos.of(
                this.contents.maxRows(),
                this.contents.maxColumns(),
                row,
                column
        );
    }

    protected @NotNull SlotPos getSlotPos(int slot) {
        return SlotPos.of(
                this.contents.maxRows(),
                this.contents.maxColumns(),
                slot
        );
    }

    protected @NotNull SlotPos getCorrectSlotPos(int slot) {
        return this.getSlotPos(slot).convertFromFrame(
                this.contents.menuSession().getRows(),
                this.contents.menuSession().getColumns(),
                this.contents.menuFrameData()
        );
    }

    protected void init(@NotNull MenuIteratorType menuIteratorType) {
        if (!this.availableSlotPositions.isEmpty()) return;

        switch (menuIteratorType) {
            case HORIZONTAL -> this.horizontalInitialization();
            case VERTICAL -> this.verticalInitialization();
        }
    }

    private void horizontalInitialization() {
        int inventorySize = this.contents.maxRows() * this.contents.maxColumns();
        Inventory inventory = getInventory();

        int slotStart = this.getStartSlotPos().getSlot();
        for (int slot = slotStart; slot < inventorySize; slot++) {
            if (this.blacklist.contains(slot)) continue;

            // Check if item can be overridden in inventory
            int inventorySlot = this.getCorrectSlotPos(slot).getSlot();
            ItemStack item = inventory.getItem(inventorySlot);
            if (!this.override && item != null && item.getType() != Material.AIR) continue;

            // Add slot to items, inventorySlot will give issues with frames
            this.availableSlotPositions.add(slot);
        }
    }

    private void verticalInitialization() {
        Inventory inventory = getInventory();

        for (int column = this.startColumn; column < this.contents.maxColumns(); column++) {
            for (int row = this.startRow; row < this.contents.maxRows(); row++) {
                int currentSlot = this.getSlotPos(row, column).getSlot();
                if (this.blacklist.contains(currentSlot)) continue;

                currentSlot = this.getCorrectSlotPos(currentSlot).getSlot();

                ItemStack item = inventory.getItem(currentSlot);
                if (!this.override && item != null && item.getType() != Material.AIR) continue;

                this.availableSlotPositions.add(currentSlot);
            }
        }
    }
}