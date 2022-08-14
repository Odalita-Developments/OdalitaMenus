package nl.tritewolf.tritemenus.items.def;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DefaultItem {

    BACK(InventoryUtils.createItemStack(Material.ARROW, "&cBack", "&7Click here to go back.")),
    CLOSE(InventoryUtils.createItemStack(Material.BARRIER, "&cClose", "&7Click here to close the menu.")),
    NEXT(InventoryUtils.createItemStack(Material.ARROW, "&aNext", "&7Click here to go to the", "&7next page.")),
    PREVIOUS(InventoryUtils.createItemStack(Material.ARROW, "&aPrevious", "&7Click here to go to the", "&7previous page.")),
    SCROLL_UP(InventoryUtils.createItemStack(Material.ARROW, "&bScroll up", "&7Click here to scroll up.")),
    SCROLL_DOWN(InventoryUtils.createItemStack(Material.ARROW, "&bScroll down", "&7Click here to scroll down.")),
    SCROLL_LEFT(InventoryUtils.createItemStack(Material.ARROW, "&bScroll left", "&7Click here to scroll left.")),
    SCROLL_RIGHT(InventoryUtils.createItemStack(Material.ARROW, "&bScroll right", "&7Click here to scroll right."));

    private final ItemStack itemStack;

    private static final Map<DefaultItem, ItemStack> ITEMS = new HashMap<>();

    static {
        for (DefaultItem defaultItem : values()) {
            ITEMS.put(defaultItem, defaultItem.getItemStack());
        }
    }

    public static void register(@NotNull DefaultItem defaultItem, @NotNull ItemStack itemStack) {
        ITEMS.put(defaultItem, itemStack);
    }

    @ApiStatus.Internal
    public static @NotNull ItemStack getItemStack(@NotNull DefaultItem defaultItem) {
        return ITEMS.get(defaultItem);
    }
}