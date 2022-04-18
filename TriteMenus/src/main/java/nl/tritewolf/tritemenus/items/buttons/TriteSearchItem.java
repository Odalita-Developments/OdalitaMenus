package nl.tritewolf.tritemenus.items.buttons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.menu.TriteMenuObject;
import nl.tritewolf.tritemenus.menu.TriteMenuProcessor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public final class TriteSearchItem implements TriteMenuItem {

    public static TriteSearchItem of(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler, @NotNull Consumer<InventoryClickEvent> newSearchQueryHandler) {
        return new TriteSearchItem(id, itemStack, searchHandler, newSearchQueryHandler);
    }

    public static TriteSearchItem of(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler) {
        return new TriteSearchItem(id, itemStack, searchHandler);
    }

    @Getter
    private final @NotNull String id;
    private final @NotNull ItemStack itemStack;

    private final @NotNull Function<InventoryClickEvent, String> searchHandler;
    private final @Nullable Consumer<InventoryClickEvent> newSearchQueryHandler;

    public TriteSearchItem(@NotNull String id, @NotNull ItemStack itemStack, @NotNull Function<InventoryClickEvent, String> searchHandler) {
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

            TriteMenuProcessor menuProcessor = TriteMenus.getTriteMenus().getTriteJection(TriteMenuProcessor.class);
            TriteMenuObject menuObject = menuProcessor.getMenus().get(event.getWhoClicked().getUniqueId()).values().stream().filter(TriteMenuObject::isHasMenuOpened).findFirst().orElse(null);

            if (menuObject != null) {
                menuObject.getSearchQueries().put(this.id, query);
            }

            if (this.newSearchQueryHandler != null) {
                this.newSearchQueryHandler.accept(event);
            }
        };
    }
}