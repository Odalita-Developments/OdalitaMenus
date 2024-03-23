package nl.odalitadevelopments.menus.menu.type;

import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public enum MenuType {

    ANVIL(InventoryType.ANVIL, (player, title) -> OdalitaMenusNMS.getInstance().createAnvilInventory(player)),
    BEACON(InventoryType.BEACON),
    BLAST_FURNACE(InventoryType.BLAST_FURNACE),
    BREWING(InventoryType.BREWING),
    CARTOGRAPHY(InventoryType.CARTOGRAPHY),
    CHEST_1_ROW(9),
    CHEST_2_ROW(18),
    CHEST_3_ROW(27),
    CHEST_4_ROW(36),
    CHEST_5_ROW(45),
    CHEST_6_ROW(54),
    CRAFTING(InventoryType.WORKBENCH, (player, title) -> OdalitaMenusNMS.getInstance().createCraftingInventory(player)),
    ENCHANTING(InventoryType.ENCHANTING, (player, title) -> OdalitaMenusNMS.getInstance().createEnchantingInventory(player)),
    FURNACE(InventoryType.FURNACE),
    GRINDSTONE(InventoryType.GRINDSTONE),
    HOPPER(InventoryType.HOPPER),
    LECTERN(InventoryType.LECTERN),
    LOOM(InventoryType.LOOM),
    SMITHING(InventoryType.SMITHING),
    SMOKER(InventoryType.SMOKER),
    STONECUTTER(InventoryType.STONECUTTER, (player, title) -> OdalitaMenusNMS.getInstance().createStonecutterInventory(player)),
    WINDOW_3X3(InventoryType.DROPPER);

    private final int size;
    private final InventoryType inventoryType;
    private final BiFunction<Player, String, Object> nmsInventoryCreation;

    MenuType(int size) {
        this.size = size;
        this.inventoryType = null;
        this.nmsInventoryCreation = null;
    }

    MenuType(@NotNull InventoryType inventoryType) {
        this.size = 0;
        this.inventoryType = inventoryType;
        this.nmsInventoryCreation = null;
    }

    MenuType(@NotNull InventoryType inventoryType, @NotNull BiFunction<Player, String, Object> nmsInventoryCreation) {
        this.size = 0;
        this.inventoryType = inventoryType;
        this.nmsInventoryCreation = nmsInventoryCreation;
    }

    @NotNull InventoryCreation createInventory(Player player, String title) {
        if (this.nmsInventoryCreation != null) {
            Object nmsInventory = this.nmsInventoryCreation.apply(player, title);
            Inventory bukkitInventory = OdalitaMenusNMS.getInstance().getInventoryFromNMS(nmsInventory);
            return new InventoryCreation(nmsInventory, bukkitInventory);
        }

        Inventory bukkitInventory;
        if (this.inventoryType == null) {
            bukkitInventory = Bukkit.getServer().createInventory(null, this.size, title);
        } else {
            bukkitInventory = Bukkit.getServer().createInventory(null, this.inventoryType, title);
        }

        return new InventoryCreation(null, bukkitInventory);
    }
}