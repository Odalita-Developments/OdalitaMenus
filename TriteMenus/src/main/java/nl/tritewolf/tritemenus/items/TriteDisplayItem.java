package nl.tritewolf.tritemenus.items;

import lombok.AllArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
public final class TriteDisplayItem implements TriteMenuItem {

    public static TriteDisplayItem of(@NotNull ItemStack itemStack) {
        return new TriteDisplayItem(itemStack);
    }

    private final @NotNull ItemStack itemStack;

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {};
    }
}