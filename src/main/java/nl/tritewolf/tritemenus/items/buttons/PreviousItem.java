package nl.tritewolf.tritemenus.items.buttons;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PreviousItem implements MenuItem {

    public static PreviousItem of(MenuProvider provider, Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        return new PreviousItem(provider, pagination, itemStack, showOnFirstPage);
    }

    public static PreviousItem of(MenuProvider provider, Pagination pagination, boolean showOnFirstPage) {
        return new PreviousItem(provider, pagination, showOnFirstPage);
    }

    public static PreviousItem of(MenuProvider provider, Pagination pagination, ItemStack itemStack) {
        return new PreviousItem(provider, pagination, itemStack);
    }

    public static PreviousItem of(MenuProvider provider, Pagination pagination) {
        return new PreviousItem(provider, pagination);
    }

    private final MenuProvider provider;
    private final Pagination pagination;
    private final ItemStack itemStack;
    private final boolean showOnFirstPage;

    public PreviousItem(MenuProvider provider, Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        this.provider = provider;
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        if (!this.showOnFirstPage && pagination.isFirst()) {
            this.itemStack = new ItemStack(Material.AIR);
            return;
        }

        this.itemStack = itemStack;
    }

    public PreviousItem(MenuProvider provider, Pagination pagination, boolean showOnFirstPage) {
        this.provider = provider;
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        if (!this.showOnFirstPage && pagination.isFirst()) {
            this.itemStack = new ItemStack(Material.AIR);
            return;
        }

        this.itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bPrevious page &8("));
        itemMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', "&7Go to the previous page.")));
        this.itemStack.setItemMeta(itemMeta);
    }

    public PreviousItem(MenuProvider provider, Pagination pagination, ItemStack itemStack) {
        this(provider, pagination, itemStack, false);
    }

    public PreviousItem(MenuProvider provider, Pagination pagination) {
        this(provider, pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player)) return;
            if (this.showOnFirstPage && this.pagination.isFirst()) return;

            MenuProcessor menuProcessor = TriteMenus.getTriteMenus().getTriteJection(MenuProcessor.class);
            if (menuProcessor == null) return;

            MenuObject menuObject = menuProcessor.getOpenMenus().get(event.getWhoClicked().getUniqueId());
            if (menuObject == null) return;

            this.pagination.setCurrentPage(this.pagination.getCurrentPage() - 1);

            MenuIterator iterator = this.pagination.getIterator().reset();

            for (Supplier<MenuItem> itemSupplier : this.pagination.getItemsOnPage()) {
                MenuItem menuItem = itemSupplier.get();
                if (menuItem == null) continue;

                iterator.set(menuItem);
                int slot = iterator.getSlot();

                menuObject.getInventory().setItem(slot, menuItem.getItemStack());

                iterator.next();
            }
        };
    }
}