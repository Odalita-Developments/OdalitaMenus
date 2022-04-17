package nl.tritewolf.tritemenus.tests;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.menu.providers.TriteGlobalMenuProvider;
import nl.tritewolf.tritemenus.menu.TriteMenuType;

@TriteMenu(
        displayName = "TestMenu",
        menuType = TriteMenuType.GLOBAL,
        rows = 4
)
public class TestMenu implements TriteGlobalMenuProvider {


    @Override
    public void onLoad(TriteInventoryContents triteInventoryContents) {
    }
}
