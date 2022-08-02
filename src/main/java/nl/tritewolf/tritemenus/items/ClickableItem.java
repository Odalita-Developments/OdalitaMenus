package nl.tritewolf.tritemenus.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClickableItem implements MenuItem {

    public static ClickableItem of(@NotNull ItemStack itemStack, @NotNull Consumer<InventoryClickEvent> clickHandler) {
        return new ClickableItem(itemStack, clickHandler);
    }

    private final @NotNull ItemStack itemStack;
    private final @NotNull Consumer<InventoryClickEvent> clickHandler;

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return this.clickHandler;
    }
}