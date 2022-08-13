package nl.tritewolf.tritemenus.menu.type.types;

import nl.tritewolf.tritemenus.menu.type.MenuType;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class DispenserMenuType implements MenuType {

    @Override
    public @NotNull InventoryType type() {
        return InventoryType.DISPENSER;
    }

    @Override
    public int maxRows() {
        return 3;
    }

    @Override
    public int maxColumns() {
        return 3;
    }
}