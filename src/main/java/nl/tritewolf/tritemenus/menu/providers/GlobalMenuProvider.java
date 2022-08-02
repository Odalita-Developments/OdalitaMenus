package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.InventoryContents;
import org.jetbrains.annotations.NotNull;

public interface GlobalMenuProvider extends MenuProvider {

    void onLoad(@NotNull InventoryContents inventoryContents);
}