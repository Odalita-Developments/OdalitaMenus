package nl.tritewolf.tritemenus.menu.type;

import nl.tritewolf.tritemenus.contents.pos.SlotPos;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public interface MenuType {

    @NotNull
    InventoryType type();

    int maxRows();

    int maxColumns();

    default @NotNull Collection<@NotNull Integer> otherSlots() {
        return Set.of();
    }

    default @NotNull Collection<@NotNull SupportedFeatures> disallowedFeatures() {
        return Set.of();
    }

    default boolean fitsInMenu(int slot) {
        boolean otherSlot = this.otherSlots().contains(slot);
        if (otherSlot) return true;

        if (this.maxColumns() == 0) return false;

        SlotPos slotPos = SlotPos.of(this.maxRows(), this.maxColumns(), slot);
        return slotPos.getRow() < this.maxRows() && slotPos.getColumn() < this.maxColumns();
    }

    default boolean isFeatureAllowed(@NotNull SupportedFeatures feature) {
        return !this.disallowedFeatures().contains(feature);
    }
}