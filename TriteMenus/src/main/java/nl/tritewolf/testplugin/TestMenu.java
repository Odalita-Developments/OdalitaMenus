package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.items.TriteDisplayItem;
import nl.tritewolf.tritemenus.menu.TriteMenuType;
import nl.tritewolf.tritemenus.menu.providers.TriteMenuProvider;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@TriteMenu(
        rows = 4,
        displayName = "testMenu",
        menuType = TriteMenuType.PLAYER
)
public class TestMenu implements TriteMenuProvider {

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

        contents.fill(TriteDisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
    }
}
