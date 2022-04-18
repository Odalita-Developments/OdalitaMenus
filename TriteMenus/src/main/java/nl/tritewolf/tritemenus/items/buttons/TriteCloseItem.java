package nl.tritewolf.tritemenus.items.buttons;

import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.items.TriteMenuItem;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
public final class TriteCloseItem implements TriteMenuItem {

    public static TriteCloseItem of(@NotNull ItemStack itemStack) {
        return new TriteCloseItem(itemStack);
    }

    public static TriteCloseItem empty() {
        return new TriteCloseItem();
    }

    private final @NotNull ItemStack itemStack;

    public TriteCloseItem() {
        this.itemStack = new ItemBuilder(Material.BARRIER)
                .setLore("&cClose")
                .build();
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