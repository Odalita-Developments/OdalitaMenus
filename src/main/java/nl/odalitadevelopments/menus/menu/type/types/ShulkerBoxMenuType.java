package nl.odalitadevelopments.menus.menu.type.types;

import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class ShulkerBoxMenuType implements MenuType {

    @Override
    public @NotNull InventoryType type() {
        return InventoryType.SHULKER_BOX;
    }

    @Override
    public int maxRows() {
        return 3;
    }

    @Override
    public int maxColumns() {
        return 9;
    }
}