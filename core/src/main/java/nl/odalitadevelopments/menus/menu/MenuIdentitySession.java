package nl.odalitadevelopments.menus.menu;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuIdentityContents;
import nl.odalitadevelopments.menus.menu.type.InventoryCreation;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class MenuIdentitySession extends AbstractMenuSession<MenuIdentitySession, Set<Player>, MenuIdentityContents<?, ?>> {

    MenuIdentitySession(OdalitaMenus instance, MenuOpenerBuilderImpl<?> builder, MenuData data, InventoryCreation inventoryData, Player player) {
        super(instance, builder, data, inventoryData, (session) -> MenuIdentityContents.create(session, data.getOrThrow(MenuData.Key.IDENTITY)));

        this.viewerEntry = new HashSet<>();
        this.viewerEntry.add(player);
    }

    @Override
    protected @NotNull MenuIdentitySession self() {
        return this;
    }

    @Override
    protected @NotNull InventoryCreation createInventoryData(@NotNull MenuType menuType, @NotNull String title) {
        return null;
    }
}