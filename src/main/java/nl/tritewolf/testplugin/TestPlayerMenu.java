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
            Bukkit.getScheduler().runTaskTimerAsynchronously(JavaPlugin.getPlugin(TestPlugin.class), () -> {
                pagination.addItem(() -> UpdatableItem.of(() -> new ItemBuilder(Material.LEATHER, "TEST PAGINATION ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(9999)).build()));
            }, 0, 2 * 20);
        }

        contents.set(45, PreviousItem.of(this, pagination));
        contents.set(53, NextItem.of(this, pagination));

       /* contents.setClickable(0, Material.DIAMOND, event -> {
            System.out.println("CLicked");
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND).build());
        });

        contents.setDisplay(1, Material.STONE);

        contents.setUpdatable(2, () -> {
            i++;
            return new ItemBuilder(Material.STONE).setDisplayName("NUMBER -> " + i + "").build();
        }, event -> {
            System.out.println("works");
        });
*/

//        List<MenuItem> menu = new ArrayList<>();
//
//        for (int j = 0; j < 30; j++) {
//            menu.add(new DisplayItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, "Plek " + j).build()));
//        }
//        contents.createSimpleIterator(MenuIteratorType.VERTICAL, 0, 0, menu);

//        contents.setDisplay(49, new ItemBuilder(Material.BOOK, "TEST " + this.test).build());

        //        contents.fill(TriteDisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }


}
