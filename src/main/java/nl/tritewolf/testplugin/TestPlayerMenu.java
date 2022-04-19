package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.annotations.TritePattern;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.items.TriteDisplayItem;
import nl.tritewolf.tritemenus.iterators.TriteIterator;
import nl.tritewolf.tritemenus.iterators.TriteIteratorType;
import nl.tritewolf.tritemenus.menu.providers.TritePlayerMenuProvider;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@TriteMenu(
        rows = 4,
        displayName = "Test menu"
)
public class TestPlayerMenu implements TritePlayerMenuProvider {

    int i = 0;

    @Override
    public void onLoad(Player player, TriteInventoryContents contents) {
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

        contents.createPatternIterator(TestPattern.class, TriteIteratorType.VERTICAL, List.of(
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
                new TriteDisplayItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                ));

//        contents.fill(TriteDisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }
}