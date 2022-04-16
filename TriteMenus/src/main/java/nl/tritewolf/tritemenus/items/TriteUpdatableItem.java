package nl.tritewolf.tritemenus.items;

import lombok.AllArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class TriteUpdatableItem implements TriteMenuItem {

    private Supplier<ItemStack> itemStack;
    private Consumer<InventoryClickEvent> click;
    private int updateTicks;

    @Override
    public ItemStack getItemStack() {
        return this.itemStack.get();
    }

    @Override
    public Consumer<InventoryClickEvent> onClick() {
        return click;
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public int getUpdateTicks() {
        return this.updateTicks;
    }
}
