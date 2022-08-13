package nl.tritewolf.tritemenus.items.buttons;

import nl.tritewolf.tritemenus.items.PageUpdatableItem;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NextItem implements PageUpdatableItem {

    public static NextItem of(Pagination pagination, ItemStack itemStack, boolean showOnLastPage) {
        return new NextItem(pagination, itemStack, showOnLastPage);
    }

    public static NextItem of(Pagination pagination, boolean showOnLastPage) {
        return new NextItem(pagination, showOnLastPage);
    }

    public static NextItem of(Pagination pagination, ItemStack itemStack) {
        return new NextItem(pagination, itemStack);
    }

    public static NextItem of(Pagination pagination) {
        return new NextItem(pagination);
    }

    private final Pagination pagination;
    private final ItemStack itemStack;
    private final boolean showOnLastPage;

    protected NextItem(Pagination pagination, ItemStack itemStack, boolean showOnLastPage) {
        this.pagination = pagination;
        this.showOnLastPage = showOnLastPage;
        this.itemStack = itemStack;
    }

    protected NextItem(Pagination pagination, boolean showOnLastPage) {
        this.pagination = pagination;
        this.showOnLastPage = showOnLastPage;

        this.itemStack = InventoryUtils.createItemStack(Material.ARROW,
                "&bNext page &8(&f" + this.pagination.getCurrentPage() + 1 + "&7/&f" + this.pagination.getCurrentPage() + "&8)", // TODO
                "&7Go to the next page."
        );
    }

    protected NextItem(Pagination pagination, ItemStack itemStack) {
        this(pagination, itemStack, false);
    }

    protected NextItem(Pagination pagination) {
        this(pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (!showOnLastPage && this.pagination.isLast()) {
            return new ItemStack(Material.AIR);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player)) return;
            if (this.pagination.isLast()) return;

            this.pagination.nextPage();
        };
    }
}