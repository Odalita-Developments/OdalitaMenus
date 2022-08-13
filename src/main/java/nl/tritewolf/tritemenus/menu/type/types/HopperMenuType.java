package nl.tritewolf.tritemenus.menu.type.types;

import nl.tritewolf.tritemenus.menu.type.MenuType;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class HopperMenuType implements MenuType {

    @Override
    public @NotNull InventoryType type() {
        return InventoryType.HOPPER;
    }

    @Override
    public int maxRows() {
        return 1;
    }

    @Override
    public int maxColumns() {
        return 5;
    }
}