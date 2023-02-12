package nl.odalitadevelopments.menus.menu.providers;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import org.jetbrains.annotations.NotNull;

public interface GlobalMenuProvider extends MenuProvider {

    void onLoad(@NotNull InventoryContents inventoryContents);
}