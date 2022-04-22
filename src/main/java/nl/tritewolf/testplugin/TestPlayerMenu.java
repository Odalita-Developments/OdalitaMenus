package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.contents.pagination.Pagination;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Menu(
        rows = 6,
        displayName = "Test menu"
)
public class TestPlayerMenu implements PlayerMenuProvider {

    private final int test;
    private int test2;

    public TestPlayerMenu(int test) {
        this.test = test;
    }

    public TestPlayerMenu() {
        this(1);
    }

    @Override
    public void onLoad(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination("TEST", 14, new MenuIterator(contents, MenuIteratorType.HORIZONTAL, 1, 1, true)
                .blacklist(17, 18));

        for (int i = 0; i < 22; i++) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLaterAsynchronously(TestPlugin.getPlugin(TestPlugin.class), () -> {
                pagination.addItem(() -> UpdatableItem.of(() -> new ItemBuilder(Material.LEATHER, "TEST PAGINATION ITEM: " + (++test2)).build(), (event) -> System.out.println("CLICKED ON " + finalI), 40));
            }, 20 + (i * 20));
        }

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
        //       contents.newTriteIterator(TriteIteratorType.HORIZONTAL, 1,1, () -> return)

        //        List<MenuItem> menu = new ArrayList<>();
        //
        //        for (int j = 0; j < 30; j++) {
        //            menu.add(new DisplayItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, "Plek " + j).build()));
        //        }
        //        contents.createDirectionsPatternIterator(TestPattern3.class, menu);

        contents.setDisplay(49, new ItemBuilder(Material.BOOK, "TEST " + this.test).build());

        //        contents.fill(TriteDisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }


}
