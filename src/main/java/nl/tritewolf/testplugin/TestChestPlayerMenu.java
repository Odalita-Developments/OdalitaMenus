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
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Bukkit;
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
            items.add(() -> DisplayItem.of(InventoryUtils.createItemStack((finalI % 7 == 0) ? Material.LEATHER : Material.COOKED_BEEF, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99))));
        }

        Scrollable scrollable = contents.scrollable("test", 4, 9)
                .items(items)
                .pattern(0, 0, TestScrollablePattern.class)
                .horizontally()
                .repeated();

        //                Scrollable scrollable = contents.scrollable("test", 4, 7)
        //                        .items(items)
        //                        .single(1, 1)
        //                        .horizontally();

        scrollable.addItem(() -> UpdatableItem.of(() -> InventoryUtils.createItemStack(Material.LEAD, "UPDATABLE ITEM: " + ThreadLocalRandom.current().nextInt(99)),
                5));

        scrollable.addItem(() -> ClickableItem.of(new ItemStack(Material.ACACIA_FENCE), (event) -> {
            event.getWhoClicked().sendMessage("Clicked!");
        }));

        contents.setClickable(49, Material.STONE_BUTTON, "Klik op mij om de titel te veranderen!", (event) -> {
            if (contents.scheduler().isRunning("close-task")) {
                event.getWhoClicked().sendMessage("Task is already running!");
                return;
            }

            contents.scheduler().schedule("close-task", () -> {
                int seconds = contents.cache("seconds", 5);

                if (seconds <= 0) {
                    event.getWhoClicked().sendMessage("closing inventory");

                    Bukkit.getScheduler().runTask(TestPlugin.getPlugin(TestPlugin.class), () -> {
                        event.getWhoClicked().closeInventory();
                    });

                    contents.pruneCache("seconds");
                    return;
                }

                contents.setTitle("Menu sluit in " + seconds);
                contents.setCache("seconds", seconds - 1);
            }, 20, 6);
        });

        contents.set(45, ScrollItem.up(scrollable));
        contents.set(46, ScrollItem.left(scrollable));
        contents.set(52, ScrollItem.right(scrollable));
        contents.set(53, ScrollItem.down(scrollable));
    }
}