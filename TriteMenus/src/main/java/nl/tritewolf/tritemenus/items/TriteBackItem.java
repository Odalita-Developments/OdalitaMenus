package nl.tritewolf.tritemenus.items;

import lombok.AllArgsConstructor;
import lombok.Setter;
import nl.tritewolf.tritemenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Setter
@AllArgsConstructor
public class TriteBackItem implements TriteMenuItem {


    private ItemStack itemStack;
    private Inventory inventory;

    public TriteBackItem() {
        this.itemStack = new ItemBuilder(Material.ARROW).setLore("&7Go back").build();
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public Consumer<InventoryClickEvent> onClick() {
        if (inventory == null) {
            //TODO add exception
        }
        return (event) -> event.getWhoClicked().openInventory(this.inventory);
    }
}
