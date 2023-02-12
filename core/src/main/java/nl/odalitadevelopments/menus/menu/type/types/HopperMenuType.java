package nl.odalitadevelopments.menus.menu.type.types;

import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import org.jetbrains.annotations.NotNull;

public final class HopperMenuType implements SupportedMenuType {

    @Override
    public @NotNull MenuType type() {
        return MenuType.HOPPER;
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