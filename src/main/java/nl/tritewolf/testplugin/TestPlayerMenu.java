package nl.tritewolf.testplugin;

import nl.tritewolf.testplugin.sb.TestExtraMenuProvider;
import nl.tritewolf.testplugin.sb.ExtraPlayer;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Menu(
        rows = 6,
        displayName = "Test menu"
)
public class TestPlayerMenu implements TestExtraMenuProvider {

    private final int test;

    public TestPlayerMenu(int test) {
        this.test = test;
    }

    public TestPlayerMenu() {
        this(1);
    }

    @Override
    public void onLoad(@NotNull Player player, ExtraPlayer extraPlayer, @NotNull InventoryContents contents) {
        Scrollable scrollable = contents.scrollable("test", 5, 9)
                .pattern(0, 0, TestScrollablePattern.class)
                .vertically()
                .continuous();

        for (int i = 0; i < 74; i++) {
            int finalI = i;
            scrollable.addItem(() -> DisplayItem.of(new ItemBuilder((finalI % 7 == 0) ? Material.LEATHER : Material.COOKED_BEEF, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99)).build()));
        }

        scrollable.addItem(() -> UpdatableItem.of(() -> new ItemBuilder(Material.LEAD)
                .setDisplayName("UPDATABLE ITEM: " + ThreadLocalRandom.current().nextInt(99))
                .build(), 5));

        contents.set(45, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
            scrollable.previous();
        }));

        contents.set(53, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
            scrollable.next();
        }));

        //        contents.set(4, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
        //            scrollable.previousVertical();
        //        }));
        //
        //        contents.set(18, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
        //            scrollable.previousHorizontal();
        //        }));
        //
        //        contents.set(26, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
        //            scrollable.nextHorizontal();
        //        }));

        //        contents.set(40, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
        //            scrollable.nextVertical();
        //        }));
    }
}