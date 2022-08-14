package nl.tritewolf.testplugin;

import nl.tritewolf.testplugin.sb.ExtraPlayer;
import nl.tritewolf.testplugin.sb.TestExtraMenuProvider;
import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.items.buttons.ScrollItem;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Menu(
        rows = 6,
        title = "Test menu"
)
public class TestChestPlayerMenu implements TestExtraMenuProvider {

    private final int test;

    public TestChestPlayerMenu(int test) {
        this.test = test;
    }

    public TestChestPlayerMenu() {
        this(1);
    }

    @Override
    public void onLoad(@NotNull Player player, ExtraPlayer extraPlayer, @NotNull InventoryContents contents) {
        List<Supplier<MenuItem>> items = new ArrayList<>();

        for (int i = 0; i < 74; i++) {
            int finalI = i;
            items.add(() -> DisplayItem.of(new ItemBuilder((finalI % 7 == 0) ? Material.LEATHER : Material.COOKED_BEEF, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99)).build()));
        }

        Scrollable scrollable = contents.scrollable("test", 5, 5)
                .items(items)
                .single(0, 2)
                .vertically();

        //                Scrollable scrollable = contents.scrollable("test", 4, 7)
        //                        .items(items)
        //                        .single(1, 1)
        //                        .horizontally();

        scrollable.addItem(() -> UpdatableItem.of(() -> new ItemBuilder(Material.LEAD)
                .setDisplayName("UPDATABLE ITEM: " + ThreadLocalRandom.current().nextInt(99))
                .build(), 5));

        scrollable.addItem(() -> ClickableItem.of(new ItemStack(Material.ACACIA_FENCE), (event) -> {
            event.getWhoClicked().sendMessage("Clicked!");
        }));

        contents.set(45, ScrollItem.up(scrollable));
        contents.set(46, ScrollItem.left(scrollable));
        contents.set(52, ScrollItem.right(scrollable));
        contents.set(53, ScrollItem.down(scrollable));
    }
}