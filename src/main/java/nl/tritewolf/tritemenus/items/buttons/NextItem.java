package nl.tritewolf.tritemenus.items.buttons;

import lombok.Getter;
import lombok.Setter;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.contents.SlotPos;
import nl.tritewolf.tritemenus.items.DisplayItem;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.iterators.MenuIterator;
import nl.tritewolf.tritemenus.menu.MenuObject;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import nl.tritewolf.tritemenus.utils.callback.ReturnableTypeCallback;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NextItem implements MenuItem {

    public static NextItem of(MenuProvider provider, Pagination pagination, ItemStack itemStack, boolean showOnLastPage) {
        return new NextItem(provider, pagination, itemStack, showOnLastPage);
    }

    public static NextItem of(MenuProvider provider, Pagination pagination, boolean showOnLastPage) {
        return new NextItem(provider, pagination, showOnLastPage);
    }

    public static NextItem of(MenuProvider provider, Pagination pagination, ItemStack itemStack) {
        return new NextItem(provider, pagination, itemStack);
    }

    public static NextItem of(MenuProvider provider, Pagination pagination) {
        return new NextItem(provider, pagination);
    }

    private final MenuProvider provider;
    private final Pagination pagination;
    private final ItemStack itemStack;
    private final boolean showOnLastPage;
    @Setter
    @Getter
    private SlotPos slot;


    public NextItem(MenuProvider provider, Pagination pagination, ItemStack itemStack, boolean showOnLastPage) {
        this.provider = provider;
        this.pagination = pagination;
        this.showOnLastPage = showOnLastPage;

        if (!this.showOnLastPage && pagination.isLast()) {
            this.itemStack = new ItemStack(Material.AIR);
            return;
        }

        this.itemStack = itemStack;
    }

    public NextItem(MenuProvider provider, Pagination pagination, boolean showOnLastPage) {
        this.provider = provider;
        this.pagination = pagination;
        this.showOnLastPage = showOnLastPage;

        if (!this.showOnLastPage && pagination.isLast()) {
            this.itemStack = new ItemStack(Material.AIR);
            return;
        }

        this.itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bNext page &8("));
        itemMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', "&7Go to the next page.")));
        this.itemStack.setItemMeta(itemMeta);
    }

    public NextItem(MenuProvider provider, Pagination pagination, ItemStack itemStack) {
        this(provider, pagination, itemStack, false);
    }

    public NextItem(MenuProvider provider, Pagination pagination) {
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
            if (this.pagination.isLast()) return;

            this.pagination.nextPage(() -> {
                if (showOnLastPage || !this.pagination.isLast()) return;
                MenuObject triteMenu = pagination.getContents().getTriteMenu();
                Inventory inventory = triteMenu.getInventory();

                int slot = event.getSlot();
                ItemStack itemStack = new ItemStack(Material.AIR);

                pagination.getContents().set(slot, new DisplayItem(itemStack));
                InventoryUtils.updateItem((Player) event.getWhoClicked(), slot, itemStack, inventory);

                int slotPreviousItem = triteMenu.getPreviousItem().getSlot().getSlot();
                pagination.getContents().set(slotPreviousItem, new PreviousItem(this.provider, this.pagination));
                InventoryUtils.updateItem((Player) event.getWhoClicked(), slotPreviousItem, triteMenu.getPreviousItem().getItemStack(), inventory);
            });
        };
    }
}