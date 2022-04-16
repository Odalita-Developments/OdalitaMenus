package nl.tritewolf.tritemenus.tests;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.menu.TriteMenuProvider;
import org.bukkit.entity.Player;

@TriteMenu(displayName = "TestMenu", rows = 4)
public class TestMenu implements TriteMenuProvider {

    @Override
    public void onLoad(Player player) {

    }
}
