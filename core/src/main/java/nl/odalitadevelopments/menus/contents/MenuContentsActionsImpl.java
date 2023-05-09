package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
final class MenuContentsActionsImpl implements MenuContentsActions {

    private final MenuContentsImpl menuContents;

    @Override
    public void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory item lore supplier is not supported in frames.");
        }

        this.menuContents.cache.setItemMetaChanger(itemMetaChanger);
    }
}