package nl.tritewolf.tritemenus.items.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.items.def.DefaultItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class CloseItem implements MenuItem {

    public static @NotNull CloseItem of(@NotNull ItemStack itemStack) {
        return new CloseItem(itemStack);
    }

    public static @NotNull CloseItem empty() {
        return new CloseItem();
    }

    private final ItemStack itemStack;

    private CloseItem() {
        this.itemStack = DefaultItem.getItemStack(DefaultItem.CLOSE);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> event.getWhoClicked().closeInventory();
    }
}