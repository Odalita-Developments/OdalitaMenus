package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.PlayerInventoryLoreApplier;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuContentsActions permits MenuContentsActionsImpl {

    void applyLoreToPlayerInventoryItemsOnOpen(@NotNull PlayerInventoryLoreApplier loreApplier);
}