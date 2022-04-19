package nl.tritewolf.tritemenus.menu.providers;

import nl.tritewolf.tritemenus.contents.TriteInventoryContents;

public abstract class TriteGlobalMenuProvider extends TriteMenuProvider {

    public abstract void onLoad(TriteInventoryContents triteInventoryContents);
}