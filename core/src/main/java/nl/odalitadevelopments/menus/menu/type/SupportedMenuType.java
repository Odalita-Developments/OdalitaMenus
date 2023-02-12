package nl.odalitadevelopments.menus.menu.type;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public interface SupportedMenuType {

    @NotNull
    MenuType type();

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

    @ApiStatus.Internal
    default @NotNull Inventory createInventory(@NotNull String title) {
        return this.type().createInventory(title);
    }
}