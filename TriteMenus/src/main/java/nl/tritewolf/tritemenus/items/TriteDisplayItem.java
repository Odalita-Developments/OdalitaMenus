package nl.tritewolf.tritemenus.items;

import lombok.AllArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@AllArgsConstructor
public class TriteDisplayItem implements TriteMenuItem {

    private ItemStack itemStack;

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public Consumer<InventoryClickEvent> onClick() {
        return (event) -> {};
    }
}
