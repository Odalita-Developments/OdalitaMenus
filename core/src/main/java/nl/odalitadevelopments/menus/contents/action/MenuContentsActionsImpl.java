package nl.odalitadevelopments.menus.contents.action;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
final class MenuContentsActionsImpl implements MenuContentsActions {

    private final AbstractMenuSession<?, ?, ?> menuSession;

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        this.menuSession.cache().setItemMetaChanger(itemMetaChanger);
    }

    @Override
    public void setProperty(@NotNull MenuProperty property, int value) {
        this.menuSession.menuProperty(property, value);
    }
}