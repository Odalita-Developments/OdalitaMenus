package nl.tritewolf.tritemenus.items.buttons;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.MenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class CloseItem implements MenuItem {

    public static @NotNull CloseItem of(@NotNull ItemStack itemStack) {
        return new CloseItem(itemStack);
    }

    public static @NotNull CloseItem get() {
        return new CloseItem();
    }

    private ItemStack itemStack;

    private CloseItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    private CloseItem() {
        this(null);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull TriteMenus instance) {
        if (this.itemStack == null) {
            this.itemStack = instance.getProvidersContainer().getDefaultItemProvider().closeItem();
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull TriteMenus instance) {
        return (event) -> event.getWhoClicked().closeInventory();
    }
}