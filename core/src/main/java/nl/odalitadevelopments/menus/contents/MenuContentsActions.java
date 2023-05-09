package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.PlayerInventoryItemMetaChanger;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuContentsActions permits MenuContentsActionsImpl {

    void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger);
}