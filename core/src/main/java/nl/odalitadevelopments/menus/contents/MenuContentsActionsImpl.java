package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.PlayerInventoryLoreApplier;
import org.jetbrains.annotations.NotNull;

record MenuContentsActionsImpl(MenuContentsImpl menuContents) implements MenuContentsActions {

    @Override
    public void applyLoreToPlayerInventoryItemsOnOpen(@NotNull PlayerInventoryLoreApplier loreApplier) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory item lore supplier is not supported in frames.");
        }

        this.menuContents.cache.setLoreApplier(loreApplier);
    }
}