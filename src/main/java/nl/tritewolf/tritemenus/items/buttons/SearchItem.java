package nl.tritewolf.tritemenus.items.buttons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public final class SearchItem implements MenuItem {

    public static SearchItem of(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler, @NotNull Consumer<InventoryClickEvent> newSearchQueryHandler) {
        return new SearchItem(id, itemStack, searchHandler, newSearchQueryHandler);
    }

    public static SearchItem of(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler) {
        return new SearchItem(id, itemStack, searchHandler);
    }

    @Getter
    private final @NotNull String id;
    private final @NotNull ItemStack itemStack;

    private final @NotNull Function<InventoryClickEvent, String> searchHandler;
    private final @Nullable Consumer<InventoryClickEvent> newSearchQueryHandler;

    public SearchItem(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler) {
        this(id, itemStack, searchHandler, null);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            String query = this.searchHandler.apply(event);
            if (query.isBlank() || query.isEmpty()) query = null;

            MenuProcessor menuProcessor = TriteMenus.getTriteMenus().getTriteJection(MenuProcessor.class);
            MenuObject openMenuObject = menuProcessor.getOpenMenus().get(event.getWhoClicked().getUniqueId());

            if (openMenuObject != null) {
                openMenuObject.getSearchQueries().put(this.id, query);
            }

            if (this.newSearchQueryHandler != null) {
                this.newSearchQueryHandler.accept(event);
            }
        };
    }
}