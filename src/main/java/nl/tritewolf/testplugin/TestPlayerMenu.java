package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Menu(
        rows = 4,
        displayName = "Test menu"
)
public class TestPlayerMenu implements PlayerMenuProvider {

    int i = 0;

    @Override
    public void onLoad(Player player, InventoryContents contents) {
        contents.setClickable(0, Material.DIAMOND, event -> {
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

//       contents.newTriteIterator(TriteIteratorType.HORIZONTAL, 1,1, () -> return)

        contents.createPatternIterator(TestPattern.class, MenuIteratorType.VERTICAL, List.of(
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new DisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                ));

//        contents.fill(TriteDisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }
}
