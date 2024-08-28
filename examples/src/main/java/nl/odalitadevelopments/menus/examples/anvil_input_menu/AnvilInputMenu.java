package nl.odalitadevelopments.menus.examples.anvil_input_menu;

import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.action.MenuProperty;
import nl.odalitadevelopments.menus.menu.providers.PlayerMenuProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Menu(
        title = "Anvil Input Example",
        type = MenuType.ANVIL
)
public final class AnvilInputMenu implements PlayerMenuProvider {

    private final Consumer<String> inputConsumer;

    public AnvilInputMenu(Consumer<String> inputConsumer) {
        this.inputConsumer = inputConsumer;
    }

    @Override
    public void onLoad(@NotNull Player player, @NotNull MenuContents contents) {
        // Set the first slot to a paper item, so the player can rename it
        contents.setDisplay(0, new ItemStack(Material.PAPER));

        // Set the repair (rename in this case) cost to 0 levels
        contents.events().onInventoryEvent(PrepareAnvilEvent.class, event -> {
            contents.actions().setProperty(MenuProperty.REPAIR_COST, 0);
        });

        // Listen for the inventory click event, to get the renamed text
        contents.events().onInventoryEvent(InventoryClickEvent.class, event -> {
            // Check if the slot is the result slot
            if (event.getSlot() != 2) return;

            // Check if the clicked item is not null or air, if that is the case the player didn't put a new name in the anvil
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || currentItem.getType().isAir()) return;

            // Close inventory
            event.getWhoClicked().closeInventory();

            // Get the renamed text
            AnvilInventory anvilInventory = (AnvilInventory) event.getInventory();
            String text = anvilInventory.getRenameText();

            // Accept the input
            this.inputConsumer.accept(text);
        });
    }
}