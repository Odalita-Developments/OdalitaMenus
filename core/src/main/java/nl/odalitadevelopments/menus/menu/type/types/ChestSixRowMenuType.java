package nl.odalitadevelopments.menus.menu.type.types;

import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import org.jetbrains.annotations.NotNull;

public final class ChestSixRowMenuType implements SupportedMenuType {

    @Override
    public @NotNull MenuType type() {
        return MenuType.CHEST_6_ROW;
    }

    @Override
    public int maxRows() {
        return 6;
    }

    @Override
    public int maxColumns() {
        return 9;
    }
}