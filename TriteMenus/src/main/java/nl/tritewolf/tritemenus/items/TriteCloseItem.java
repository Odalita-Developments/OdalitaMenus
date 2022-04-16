package nl.tritewolf.tritemenus.items;

import lombok.AllArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Setter
@AllArgsConstructor
public class TriteCloseItem implements TriteMenuItem {

    private ItemStack itemStack;

    public TriteCloseItem() {
        this.itemStack = new ItemBuilder(Material.BARRIER).setLore("&7Close").build();
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public Consumer<InventoryClickEvent> onClick() {
        return (event) -> event.getWhoClicked().closeInventory();
    }
}
