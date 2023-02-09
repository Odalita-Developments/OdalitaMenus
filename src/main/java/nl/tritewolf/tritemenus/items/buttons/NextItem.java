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

public final class NextItem implements PageUpdatableItem {

    public static @NotNull NextItem of(@NotNull Pagination pagination, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new NextItem(pagination, itemStack, showOnLastPage);
    }

    public static @NotNull NextItem of(@NotNull Pagination pagination, boolean showOnLastPage) {
        return new NextItem(pagination, showOnLastPage);
    }

    public static @NotNull NextItem of(@NotNull Pagination pagination, @NotNull ItemStack itemStack) {
        return new NextItem(pagination, itemStack);
    }

    public static @NotNull NextItem of(@NotNull Pagination pagination) {
        return new NextItem(pagination);
    }

    private final Pagination pagination;
    private final boolean showOnLastPage;

    private ItemStack itemStack;

    private NextItem(Pagination pagination, ItemStack itemStack, boolean showOnLastPage) {
        this.pagination = pagination;
        this.showOnLastPage = showOnLastPage;
        this.itemStack = itemStack;
    }

    private NextItem(Pagination pagination, boolean showOnLastPage) {
        this(pagination, null, showOnLastPage);
    }

    private NextItem(Pagination pagination, ItemStack itemStack) {
        this(pagination, itemStack, false);
    }

    private NextItem(Pagination pagination) {
        this(pagination, false);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull TriteMenus instance) {
        if (!this.showOnLastPage && this.pagination.isLastPage()) {
            return new ItemStack(Material.AIR);
        }

        if (this.itemStack == null) {
            this.itemStack = instance.getProvidersContainer().getDefaultItemProvider().nextPageItem(this.pagination);
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

            if (!this.pagination.isLastPage()) {
                this.pagination.nextPage();
            }
        };
    }
}