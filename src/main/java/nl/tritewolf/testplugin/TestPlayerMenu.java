package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.items.buttons.NextItem;
import nl.tritewolf.tritemenus.items.buttons.PreviousItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

@Menu(
        rows = 6,
        displayName = "Test menu"
)
public class TestPlayerMenu implements PlayerMenuProvider {

    private final int test;

    public TestPlayerMenu(int test) {
        this.test = test;
    }

    public TestPlayerMenu() {
        this(1);
    }

    @Override
    public void onLoad(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination("TEST", 14, new MenuIterator(MenuIteratorType.HORIZONTAL, contents, 1, 1)
                .blacklist(17, 18).setOverride(true));

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(TestPlugin.class), () -> {
                pagination.addItem(() -> DisplayItem.of(new ItemBuilder(Material.LEATHER, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99)).build()));
            }, i * 20);

        }

        contents.setPageSwitchUpdateItem(45, PreviousItem.of(this, pagination));
        contents.setPageSwitchUpdateItem(53, NextItem.of(this, pagination));
    }


}
