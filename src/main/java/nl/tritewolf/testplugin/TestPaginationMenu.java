package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.items.buttons.BackItem;
import nl.tritewolf.tritemenus.items.buttons.NextItem;
import nl.tritewolf.tritemenus.items.buttons.PreviousItem;
import nl.tritewolf.tritemenus.iterators.MenuIteratorType;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Menu(
        title = "&aTest &cPagination &eMenu",
        rows = 6
)
public class TestPaginationMenu implements PlayerMenuProvider {

    @Override
    public void onLoad(@NotNull Player player, @NotNull InventoryContents contents) {
        Pagination pagination = contents.pagination("test", 28)
                .iterator(() -> contents.createIterator("iterator", MenuIteratorType.HORIZONTAL, 0, 1)
                        .blacklist(8, 9, 17, 18, 26, 27))
                .create();

        for (int i = 0; i < 200; i++) {
            int finalI = i;
            pagination.addItem(() -> DisplayItem.of(InventoryUtils.createItemStack((finalI % 7 == 0) ? Material.LEATHER : Material.COOKED_BEEF, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99))));
        }

        pagination.addItem(() -> UpdatableItem.of(() -> InventoryUtils.createItemStack(Material.LEAD, "UPDATABLE ITEM: " + ThreadLocalRandom.current().nextInt(99)),
                5
        ));

        pagination.addItem(() -> ClickableItem.of(new ItemStack(Material.ACACIA_FENCE), (event) -> {
            event.getWhoClicked().sendMessage("Clicked!");
        }));

        contents.fillRow(4, DisplayItem.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        contents.set(45, PreviousItem.of(pagination));
        contents.set(53, NextItem.of(pagination));
    }
}