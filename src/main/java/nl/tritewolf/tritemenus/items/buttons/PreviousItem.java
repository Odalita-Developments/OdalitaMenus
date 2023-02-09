package nl.tritewolf.tritemenus.items.buttons;

import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.PageUpdatableItem;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.utils.cooldown.Cooldown;
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
    private final boolean showOnFirstPage;

    private ItemStack itemStack;

    private PreviousItem(Pagination pagination, ItemStack itemStack, boolean showOnFirstPage) {
        this.pagination = pagination;
        this.showOnFirstPage = showOnFirstPage;

        this.itemStack = itemStack;
    }

    private PreviousItem(Pagination pagination, boolean showOnFirstPage) {
        this(pagination, null, showOnFirstPage);
    }

    private PreviousItem(Pagination pagination, ItemStack itemStack) {
        this(pagination, itemStack, false);
    }

    private PreviousItem(Pagination pagination) {
        this(pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull TriteMenus instance) {
        if (!this.showOnFirstPage && this.pagination.isFirstPage()) {
            return new ItemStack(Material.AIR);
        }

        if (this.itemStack == null) {
            this.itemStack = instance.getProvidersContainer().getDefaultItemProvider().previousPageItem(this.pagination);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull TriteMenus instance) {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            Cooldown cooldown = instance.getProvidersContainer().getCooldownProvider().pageCooldown();
            if (cooldown != null && instance.getCooldownContainer().checkAndCreate(player.getUniqueId(), "INTERNAL_PAGE_COOLDOWN", cooldown)) {
                return;
            }

            if (!this.pagination.isFirstPage()) {
                this.pagination.previousPage();
            }
        };
    }
}