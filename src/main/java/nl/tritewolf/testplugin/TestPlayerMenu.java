package nl.tritewolf.testplugin;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ClickableItem;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.UpdatableItem;
import nl.tritewolf.tritemenus.menu.providers.PlayerMenuProvider;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import nl.tritewolf.tritemenus.scrollable.ScrollableBuilder;
import nl.tritewolf.tritemenus.scrollable.pattern.DirectionScrollablePattern;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Menu(
        rows = 5,
        displayName = "Test menu"
)
public class TestPlayerMenu implements PlayerMenuProvider {

    private final int test;

    public TestPlayerMenu(int test) {
        this.test = test;
    }

    public TestPlayerMenu() {
        this(1);
    }

    @Override
    public void onLoad(@NotNull Player player, @NotNull InventoryContents contents) {
        Scrollable scrollable = contents.scrollable("test", 3, 7)
                .pattern(1, 1, new DirectionScrollablePattern(List.of(
                        "01|03|04|X|05|06|10|11|12|X|X|X|14",
                        "X|02|07|08|X|09|X|X|X|X|X|13|15",
                        "16|18|19|X|20|21|25|26|27|X|X|X|29",
                        "X|17|22|23|X|24|X|X|X|X|X|28|30",
                        "31|33|34|X|35|36|40|41|42|X|X|X|44",
                        "X|32|37|38|X|39|X|X|X|X|X|43|45",
                        "46|48|49|X|50|51|55|56|57|X|X|X|49",
                        "X|47|52|53|X|54|X|X|X|X|X|58|60",
                        "61|63|64|X|65|66|70|71|72|X|X|X|74",
                        "X|62|67|68|X|69|X|X|X|X|X|73|75"
                )))
                .direction(ScrollableBuilder.PatternDirection.ALL)
                .create();

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            scrollable.addItem(() -> DisplayItem.of(new ItemBuilder((finalI % 7 == 0) ? Material.LEATHER : Material.COOKED_BEEF, "ITEM: " + finalI + " / " + ThreadLocalRandom.current().nextInt(99)).build()));
        }

        scrollable.addItem(() -> UpdatableItem.of(() -> new ItemBuilder(Material.LEAD)
                .setDisplayName("UPDATABLE ITEM: " + ThreadLocalRandom.current().nextInt(99))
                .build(), 5));

        contents.set(4, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
            scrollable.previousVertical();
        }));

        contents.set(18, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
            scrollable.previousHorizontal();
        }));

        contents.set(26, ClickableItem.of(new ItemStack(Material.ARROW), event -> {
            scrollable.nextHorizontal();
        }));

        contents.set(40, ClickableItem.of(new ItemStack(Material.ARROW), (event) -> {
            scrollable.nextVertical();
        }));
    }
}