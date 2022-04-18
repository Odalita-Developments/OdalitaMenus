package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.TriteMenu;
import nl.tritewolf.tritemenus.contents.TriteInventoryContents;
import nl.tritewolf.tritemenus.contents.TriteSlotPos;
import nl.tritewolf.tritemenus.items.TriteClickableItem;
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

    @Override
    public void onLoad(Player player, TriteInventoryContents contents) {
        contents.set(0, new TriteClickableItem(new ItemBuilder(Material.DIAMOND).build(), event -> {
            System.out.println("CLicked");
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND).build());
        }));
        contents.set(1, new TriteDisplayItem(new ItemBuilder(Material.STONE).build()));
    }
}
