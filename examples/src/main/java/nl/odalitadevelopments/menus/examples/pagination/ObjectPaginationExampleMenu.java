package nl.odalitadevelopments.menus.examples.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.examples.common.EnumFilter;
import nl.odalitadevelopments.menus.examples.common.ItemBuilder;
import nl.odalitadevelopments.menus.items.ClickableItem;
import nl.odalitadevelopments.menus.items.DisplayItem;
import nl.odalitadevelopments.menus.items.buttons.CloseItem;
import nl.odalitadevelopments.menus.items.buttons.PageItem;
import nl.odalitadevelopments.menus.iterators.MenuIteratorType;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.pagination.ObjectPagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Menu(
        title = "Object Pagination Example",
        type = MenuType.CHEST_6_ROW
)
public final class ObjectPaginationExampleMenu implements PlayerMenuProvider {

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents) {
        // Create an object pagination for players with 36 items per page
        ObjectPagination<Player> pagination = contents.pagination("player_example_pagination", 36)
                .objectIterator(this.createObjectIterator(contents))
                .create();

        // Add all online players to the pagination, (optionally) use a batch to improve performance
        pagination.createBatch();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            pagination.addItem(onlinePlayer);
        }
        pagination.endBatch();

        contents.fillRow(4, DisplayItem.of(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE, "&0").build()));

        contents.set(45, PageItem.previous(pagination)); // Create previous page item with the itemstack provided in DefaultItemProvider
        contents.set(53, PageItem.next(pagination)); // Create next page item with the itemstack provided in DefaultItemProvider

        contents.set(49, CloseItem.get()); // Create close item with the itemstack provided in DefaultItemProvider

        // Sort example, same can be applied to filters
        contents.setRefreshable(51, () -> {
            // Get the current sorter from the cache
            PlayerSorter cachedSorter = contents.cache("sorter", PlayerSorter.NAME_ASCENDING);

            return ClickableItem.of(ItemBuilder.of(Material.HOPPER)
                    .displayName("&aSort")
                    .lore((lore) -> {
                        lore.add("");

                        for (PlayerSorter sorter : PlayerSorter.values()) {
                            boolean currentSorter = sorter == cachedSorter;
                            if (currentSorter) {
                                lore.add("&d- &e" + sorter.getName());
                            } else {
                                lore.add("&8- &7" + sorter.getName());
                            }
                        }

                        PlayerSorter next = cachedSorter.next();
                        PlayerSorter previous = cachedSorter.previous();

                        lore.add("");

                        lore.add("&fLeft click to sort by &a" + previous.getName() + "&f.");
                        lore.add("&fRight click to sort by &a" + next.getName() + "&f.");
                        return lore;
                    })
                    .build(), (event) -> {
                // (optional/possibly) Add cooldown to prevent spamming in case of a sorter or filter that is slow

                // Get the next or previous sorter based on the click type and apply it to the pagination
                PlayerSorter next = event.isRightClick() ? cachedSorter.next() : cachedSorter.previous();
                pagination.sorter(0, next.getComparator());
                pagination.apply();

                // Cache the new sorter and refresh the item to update the lore
                contents.setCache("sorter", next);
                contents.refreshItem(51);
            });
        });
    }

    private MenuObjectIterator<Player> createObjectIterator(MenuContents contents) {
        // Create a horizontal object iterator for player objects
        return contents.createObjectIterator(MenuIteratorType.HORIZONTAL, 0, 0, Player.class, (player) -> {
                    // Create a display item for the player with their skull and name
                    return DisplayItem.of(ItemBuilder.of(Material.PLAYER_HEAD, "&e" + player.getName())
                            .meta(SkullMeta.class, (meta) -> {
                                meta.setOwningPlayer(player);
                            })
                            .build()
                    );
                })
                .sorter(0, PlayerSorter.NAME_ASCENDING.getComparator()); // Apply the default sorting
    }

    @Getter
    @AllArgsConstructor
    public enum PlayerSorter implements EnumFilter<PlayerSorter> {

        NAME_ASCENDING("Name (A-Z)", Comparator.comparing(Player::getName)),
        NAME_DESCENDING("Name (Z-A)", Comparator.comparing(Player::getName).reversed()),
        EXPERIENCE_ASCENDING("Experience (Low-High)", Comparator.comparingInt(Player::getTotalExperience)),
        EXPERIENCE_DESCENDING("Experience (High-Low)", Comparator.comparingInt(Player::getTotalExperience).reversed());

        private final String name;
        private final Comparator<Player> comparator;
    }
}