package nl.odalitadevelopments.menus.menu.providers.frame;

import nl.odalitadevelopments.menus.contents.InventoryContents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface GlobalMenuFrameProvider extends MenuFrameProvider {

    void onLoad(@NotNull InventoryContents inventoryContents);
}