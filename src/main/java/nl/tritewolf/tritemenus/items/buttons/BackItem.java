package nl.tritewolf.tritemenus.items.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BackItem implements MenuItem {

    public static BackItem of(@NotNull ItemStack itemStack, @NotNull Inventory inventory) {
        return new BackItem(itemStack, inventory);
    }

    public static BackItem of(@NotNull Inventory inventory) {
        return new BackItem(inventory);
    }

    private final @NotNull ItemStack itemStack;
    private final @NotNull Inventory inventory;

    private BackItem(@NotNull Inventory inventory) {
        this.inventory = inventory;

        this.itemStack = new ItemBuilder(Material.ARROW)
                .setLore("&7Go back")
                .build();
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> event.getWhoClicked().openInventory(this.inventory);
    }
}