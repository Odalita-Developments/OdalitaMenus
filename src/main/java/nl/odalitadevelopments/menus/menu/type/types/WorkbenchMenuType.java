package nl.odalitadevelopments.menus.menu.type.types;

import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public final class WorkbenchMenuType implements MenuType {

    @Override
    public @NotNull InventoryType type() {
        return InventoryType.WORKBENCH;
    }

    @Override
    public int maxRows() {
        return 3;
    }

    @Override
    public int maxColumns() {
        return 3;
    }

    @Override
    public @NotNull Collection<@NotNull Integer> otherSlots() {
        return Set.of(0);
    }
}