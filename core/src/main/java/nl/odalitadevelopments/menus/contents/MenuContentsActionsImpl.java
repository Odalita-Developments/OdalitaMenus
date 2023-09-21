package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
final class MenuContentsActionsImpl implements MenuContentsActions {

    private final MenuContentsImpl menuContents;

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        this.menuContents.cache.setItemMetaChanger(itemMetaChanger);
    }

    @Override
    public void setProperty(@NotNull MenuProperty property, int value) {
        this.menuContents.menuSession.setMenuProperty(property, value);
    }
}