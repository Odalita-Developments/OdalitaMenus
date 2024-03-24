package nl.odalitadevelopments.menus.menu.type;

import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public enum MenuType {

    ANVIL(InventoryType.ANVIL, (player) -> OdalitaMenusNMS.getInstance().createAnvilInventory(player)),
    BEACON(InventoryType.BEACON), // block entity
    BLAST_FURNACE(InventoryType.BLAST_FURNACE), // block entity
    BREWING(InventoryType.BREWING), // block entity
    CARTOGRAPHY(InventoryType.CARTOGRAPHY, (player) -> OdalitaMenusNMS.getInstance().createCartographyInventory(player)),
    CHEST_1_ROW(9),
    CHEST_2_ROW(18),
    CHEST_3_ROW(27),
    CHEST_4_ROW(36),
    CHEST_5_ROW(45),
    CHEST_6_ROW(54),
    CRAFTING(InventoryType.WORKBENCH, (player) -> OdalitaMenusNMS.getInstance().createCraftingInventory(player)),
    ENCHANTING(InventoryType.ENCHANTING, (player) -> OdalitaMenusNMS.getInstance().createEnchantingInventory(player)),
    FURNACE(InventoryType.FURNACE), // block entity
    GRINDSTONE(InventoryType.GRINDSTONE), // No recipe matching
    HOPPER(InventoryType.HOPPER), // block entity
    LECTERN(InventoryType.LECTERN), // block entity
    LOOM(InventoryType.LOOM, (player) -> OdalitaMenusNMS.getInstance().createLoomInventory(player)),
    SMITHING(InventoryType.SMITHING, (player) -> OdalitaMenusNMS.getInstance().createSmithingInventory(player)),
    SMOKER(InventoryType.SMOKER), // block entity
    STONECUTTER(InventoryType.STONECUTTER, (player) -> OdalitaMenusNMS.getInstance().createStonecutterInventory(player)),
    WINDOW_3X3(InventoryType.DROPPER); // No recipe matching

    private final int size;
    private final InventoryType inventoryType;
    private final Function<Player, Object> nmsInventoryCreation;

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

    MenuType(@NotNull InventoryType inventoryType, @NotNull Function<Player, Object> nmsInventoryCreation) {
        this.size = 0;
        this.inventoryType = inventoryType;
        this.nmsInventoryCreation = nmsInventoryCreation;
    }

    @NotNull InventoryCreation createInventory(Player player, String title) {
        if (this.nmsInventoryCreation != null) {
            Object nmsInventory = this.nmsInventoryCreation.apply(player);
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