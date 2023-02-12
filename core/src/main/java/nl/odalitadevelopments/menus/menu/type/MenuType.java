package nl.odalitadevelopments.menus.menu.type;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public enum MenuType {

    ANVIL(InventoryType.ANVIL),
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
    CRAFTING(InventoryType.WORKBENCH),
    ENCHANTING(InventoryType.ENCHANTING),
    FURNACE(InventoryType.FURNACE),
    GRINDSTONE(InventoryType.GRINDSTONE),
    HOPPER(InventoryType.HOPPER),
    LECTERN(InventoryType.LECTERN),
    LOOM(InventoryType.LOOM),
    SMITHING(InventoryType.SMITHING),
    SMOKER(InventoryType.SMOKER),
    STONE_CUTTER(InventoryType.STONECUTTER),
    WINDOW_3X3(InventoryType.DROPPER);

    private final int size;
    private final InventoryType inventoryType;

    MenuType(int size) {
        this.size = size;
        this.inventoryType = null;
    }

    MenuType(@NotNull InventoryType inventoryType) {
        this.size = 0;
        this.inventoryType = inventoryType;
    }

    @NotNull Inventory createInventory(String title) {
        if (this.inventoryType == null) {
            return Bukkit.getServer().createInventory(null, this.size, title);
        } else {
            return Bukkit.getServer().createInventory(null, this.inventoryType, title);
        }
    }
}