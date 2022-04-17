package nl.tritewolf.tritemenus.items.buttons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class TriteSearchItem implements TriteMenuItem {

    public static TriteSearchItem of(String id, ItemStack itemStack, Function<InventoryClickEvent, String> searchHandler, Consumer<InventoryClickEvent> newSearchQueryHandler) {
        return new TriteSearchItem(id, itemStack, searchHandler, newSearchQueryHandler);
    }

    public static TriteSearchItem of(String id, ItemStack itemStack, Function<InventoryClickEvent, String> searchHandler) {
        return new TriteSearchItem(id, itemStack, searchHandler);
    }

    @Getter
    private final String id;
    @Getter
    private final ItemStack itemStack;

    private final Function<InventoryClickEvent, String> searchHandler;
    private final Consumer<InventoryClickEvent> newSearchQueryHandler;

    @Getter
    private String query = null;

    public TriteSearchItem(String id, ItemStack itemStack, Function<InventoryClickEvent, String> searchHandler) {
        this.id = id;
        this.itemStack = itemStack;
        this.searchHandler = searchHandler;
        this.newSearchQueryHandler = null;
    }

    @Override
    public Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            this.query = this.searchHandler.apply(event);

            if (this.newSearchQueryHandler != null) {
                this.newSearchQueryHandler.accept(event);
            }
        };
    }
}