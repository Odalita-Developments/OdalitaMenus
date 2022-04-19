package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.TriteInventoryContents;

public interface TriteGlobalMenuProvider extends TriteMenuProvider {

    void onLoad(TriteInventoryContents triteInventoryContents);
}