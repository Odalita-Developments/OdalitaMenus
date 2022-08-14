package nl.tritewolf.tritemenus.items.buttons;

import nl.tritewolf.tritemenus.items.PageUpdatableItem;
import nl.tritewolf.tritemenus.pagination.Pagination;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class PreviousItem implements PageUpdatableItem {

    public static PreviousItem of(Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        return new PreviousItem(pagination, itemStack, showOnFirstPage);
    }

    public static PreviousItem of(Pagination pagination, boolean showOnFirstPage) {
        return new PreviousItem(pagination, showOnFirstPage);
    }

    public static PreviousItem of(Pagination pagination, ItemStack itemStack) {
        return new PreviousItem(pagination, itemStack);
    }

    public static PreviousItem of(Pagination pagination) {
        return new PreviousItem(pagination);
    }

    private final Pagination pagination;
    private final ItemStack itemStack;
    private final boolean showOnFirstPage;

    protected PreviousItem(Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        this.itemStack = itemStack;
    }

    protected PreviousItem(Pagination pagination, boolean showOnFirstPage) {
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        this.itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bPrevious page &8("));
        itemMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', "&7Go to the previous page.")));
        this.itemStack.setItemMeta(itemMeta);
    }

    protected PreviousItem(Pagination pagination, ItemStack itemStack) {
        this(pagination, itemStack, false);
    }

    protected PreviousItem(Pagination pagination) {
        this(pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (!this.showOnFirstPage && pagination.isFirstPage()) {
            return new ItemStack(Material.AIR);
        }
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player)) return;
            if (this.pagination.isFirstPage()) return;

            pagination.previousPage();
        };
    }
}