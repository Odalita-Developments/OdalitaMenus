package nl.odalitadevelopments.menus.menu.type.types;

import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import org.jetbrains.annotations.NotNull;

public final class ChestFiveRowMenuType implements SupportedMenuType {

    @Override
    public @NotNull MenuType type() {
        return MenuType.CHEST_5_ROW;
    }

    @Override
    public int maxRows() {
        return 5;
    }

    @Override
    public int maxColumns() {
        return 9;
    }
}