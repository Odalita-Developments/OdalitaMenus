package nl.odalitadevelopments.menus.menu;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.menu.type.InventoryCreation;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MenuSession extends AbstractMenuSession<MenuSession, Player, MenuContents> {

    MenuSession(OdalitaMenus instance, MenuOpenerBuilderImpl<?> builder, MenuData data, InventoryCreation inventoryData, Player player) {
        super(instance, builder, data, inventoryData, MenuContents::create);

        this.viewerEntry = player;
    }

    public @NotNull Player viewer() {
        return this.viewerEntry;
    }

    @Override
    protected @NotNull MenuSession self() {
        return this;
    }

    @Override
    protected @NotNull InventoryCreation createInventoryData(@NotNull MenuType menuType, @NotNull String title) {
        return null;
    }
}