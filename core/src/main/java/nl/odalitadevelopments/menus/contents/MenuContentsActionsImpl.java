package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
final class MenuContentsActionsImpl implements MenuContentsActions {

    private final MenuContentsImpl menuContents;

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory item meta changer is not supported in frames.");
        }

        this.menuContents.cache.setItemMetaChanger(itemMetaChanger);
    }

    @Override
    public void setProperty(@NotNull MenuProperty property, int value) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Property setter is not supported in frames.");
        }

        this.menuContents.menuSession.setMenuProperty(property, value);
    }
}