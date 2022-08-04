package nl.tritewolf.tritemenus.items.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchItem implements MenuItem {

    public static @NotNull SearchItem of(@NotNull String id, @NotNull ItemStack itemStack,
                                         @NotNull Function<@NotNull InventoryClickEvent, @NotNull String> searchHandler,
                                         @NotNull Consumer<@NotNull InventoryClickEvent> newSearchQueryHandler) {
        return new SearchItem(id, itemStack, searchHandler, newSearchQueryHandler);
    }

    public static @NotNull SearchItem of(@NotNull String id, @NotNull ItemStack itemStack,
                                         @NotNull Function<@NotNull InventoryClickEvent, @NotNull String> searchHandler) {
        return new SearchItem(id, itemStack, searchHandler);
    }

    @Getter
    private final String id;
    private final ItemStack itemStack;

    private final Function<InventoryClickEvent, String> searchHandler;
    private final Consumer<InventoryClickEvent> newSearchQueryHandler;

    private SearchItem(String id, ItemStack itemStack, Function<InventoryClickEvent, String> searchHandler) {
        this(id, itemStack, searchHandler, null);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            String query = this.searchHandler.apply(event);
            if (query.isBlank() || query.isEmpty()) query = null;

            MenuProcessor menuProcessor = TriteMenus.getTriteMenus().getTriteJection(MenuProcessor.class);
            MenuObject openMenuObject = menuProcessor.getOpenMenus().get(player);

            if (openMenuObject != null) {
                openMenuObject.getSearchQueries().put(this.id, query);
            }

            if (this.newSearchQueryHandler != null) {
                this.newSearchQueryHandler.accept(event);
            }
        };
    }
}