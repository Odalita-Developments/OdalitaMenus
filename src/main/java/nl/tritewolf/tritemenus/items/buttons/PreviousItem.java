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

public final class PreviousItem implements PageUpdatableItem {

    public static @NotNull PreviousItem of(@NotNull Pagination pagination, @NotNull ItemStack itemStack, boolean showOnFirstPage) {
        return new PreviousItem(pagination, itemStack, showOnFirstPage);
    }

    public static @NotNull PreviousItem of(@NotNull Pagination pagination, boolean showOnFirstPage) {
        return new PreviousItem(pagination, showOnFirstPage);
    }

    public static @NotNull PreviousItem of(@NotNull Pagination pagination, @NotNull ItemStack itemStack) {
        return new PreviousItem(pagination, itemStack);
    }

    public static @NotNull PreviousItem of(@NotNull Pagination pagination) {
        return new PreviousItem(pagination);
    }

    private final Pagination pagination;
    private final ItemStack itemStack;
    private final boolean showOnFirstPage;

    private PreviousItem(Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        this.itemStack = itemStack;
    }

    private PreviousItem(Pagination pagination, boolean showOnFirstPage) {
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        this.itemStack = InventoryUtils.createItemStack(Material.ARROW, "&bPrevious page");
    }

    private PreviousItem(Pagination pagination, ItemStack itemStack) {
        this(pagination, itemStack, false);
    }

    private PreviousItem(Pagination pagination) {
        this(pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (!this.showOnFirstPage && this.pagination.isFirstPage()) {
            return new ItemStack(Material.AIR);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player)) return;
            if (this.pagination.isFirstPage()) return;

            this.pagination.previousPage();
        };
    }
}