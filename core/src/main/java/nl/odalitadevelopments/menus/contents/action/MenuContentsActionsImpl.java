package nl.odalitadevelopments.menus.contents.action;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
final class MenuContentsActionsImpl implements MenuContentsActions {

    private final MenuSession menuSession;

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        this.menuSession.getCache().setItemMetaChanger(itemMetaChanger);
    }

    @Override
    public void setProperty(@NotNull MenuProperty property, int value) {
        this.menuSession.setMenuProperty(property, value);
    }
}