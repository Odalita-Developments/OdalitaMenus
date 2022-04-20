package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.InventoryContents;

public interface GlobalMenuProvider extends MenuProvider {

    void onLoad(InventoryContents inventoryContents);
}