package nl.tritewolf.tritemenus.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.TriteMenus;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DisplayItem implements MenuItem {

    public static @NotNull DisplayItem of(@NotNull ItemStack itemStack) {
        return new DisplayItem(itemStack);
    }

    private final ItemStack itemStack;

    @Override
    public @NotNull ItemStack getItemStack(@NotNull TriteMenus instance) {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull TriteMenus instance) {
        return (event) -> {};
    }
}