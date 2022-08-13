package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

@Menu(
        displayName = "Test dropper",
        inventoryType = InventoryType.DROPPER
)
public class TestDropperPlayerMenu implements PlayerMenuProvider {

    @Override
    public void onLoad(@NotNull Player player, @NotNull InventoryContents contents) {
        contents.setDisplay(3, Material.ACACIA_BOAT, "Prachtige boat!");

        contents.setClickable(4, Material.ACACIA_BOAT, "Prachtige 2e boat!", (event) -> {
            event.getWhoClicked().sendMessage("TEST!");
        });
    }
}