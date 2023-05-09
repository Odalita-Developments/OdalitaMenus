package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import org.jetbrains.annotations.NotNull;

record MenuContentsActionsImpl(MenuContentsImpl menuContents) implements MenuContentsActions {

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory item lore supplier is not supported in frames.");
        }

        this.menuContents.cache.setItemMetaChanger(itemMetaChanger);
    }
}