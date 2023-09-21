package nl.odalitadevelopments.menus.contents.action;

import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuContentsActions permits MenuContentsActionsImpl {

    @Contract("_ -> new")
    @ApiStatus.Internal
    static @NotNull MenuContentsActions create(@NotNull MenuSession session) {
        return new MenuContentsActionsImpl(session);
    }

    void changeItemMetaInPlayerInventory(@NotNull PlayerInventoryItemMetaChanger itemMetaChanger);

    void setProperty(@NotNull MenuProperty property, int value);
}