package nl.odalitadevelopments.menus.items.buttons;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PageItem extends PageUpdatableItem {

    public static @NotNull PageItem previous(@NotNull IPagination<?, ?> pagination, @NotNull ItemStack itemStack, boolean showOnFirstPage) {
        return new PageItem(Type.PREVIOUS, pagination, itemStack, showOnFirstPage);
    }

    public static @NotNull PageItem previous(@NotNull IPagination<?, ?> pagination, boolean showOnFirstPage) {
        return new PageItem(Type.PREVIOUS, pagination, showOnFirstPage);
    }

    public static @NotNull PageItem previous(@NotNull IPagination<?, ?> pagination, @NotNull ItemStack itemStack) {
        return new PageItem(Type.PREVIOUS, pagination, itemStack, false);
    }

    public static @NotNull PageItem previous(@NotNull IPagination<?, ?> pagination) {
        return new PageItem(Type.PREVIOUS, pagination, false);
    }

    public static @NotNull PageItem next(@NotNull IPagination<?, ?> pagination, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new PageItem(Type.NEXT, pagination, itemStack, showOnLastPage);
    }

    public static @NotNull PageItem next(@NotNull IPagination<?, ?> pagination, boolean showOnLastPage) {
        return new PageItem(Type.NEXT, pagination, showOnLastPage);
    }

    public static @NotNull PageItem next(@NotNull IPagination<?, ?> pagination, @NotNull ItemStack itemStack) {
        return new PageItem(Type.NEXT, pagination, itemStack, false);
    }

    public static @NotNull PageItem next(@NotNull IPagination<?, ?> pagination) {
        return new PageItem(Type.NEXT, pagination, false);
    }

    private final Type type;
    private final IPagination<?, ?> pagination;
    private final boolean showOnFirstOrLastPage;

    private final ItemStack itemStack;

    private PageItem(Type type, IPagination<?, ?> pagination, ItemStack itemStack, boolean showOnFirstOrLastPage) {
        this.type = type;
        this.pagination = pagination;
        this.itemStack = itemStack;
        this.showOnFirstOrLastPage = showOnFirstOrLastPage;
    }

    private PageItem(Type type, IPagination<?, ?> pagination, boolean showOnFirstOrLastPage) {
        this(type, pagination, null, showOnFirstOrLastPage);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance) {
        if (!this.showOnFirstOrLastPage && !this.canBeUsed()) {
            return new ItemStack(Material.AIR);
        }

        if (this.itemStack == null) {
            DefaultItemProvider defaultItemProvider = instance.getProvidersContainer().getDefaultItemProvider();
            return switch (this.type) {
                case PREVIOUS -> defaultItemProvider.previousPageItem(this.pagination);
                case NEXT -> defaultItemProvider.nextPageItem(this.pagination);
            };
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance) {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            Cooldown cooldown = instance.getProvidersContainer().getCooldownProvider().pageCooldown();
            if (cooldown != null && instance.getCooldownContainer().checkAndCreate(player.getUniqueId(), "INTERNAL_PAGE_COOLDOWN", cooldown)) {
                return;
            }

            if (this.canBeUsed()) {
                this.type.handle(this.pagination);
            }
        };
    }

    private boolean canBeUsed() {
        return (this.type == Type.PREVIOUS && !this.pagination.isFirstPage())
                || (this.type == Type.NEXT && !this.pagination.isLastPage());
    }

    private enum Type {

        PREVIOUS {
            @Override
            void handle(@NotNull IPagination<?, ?> pagination) {
                pagination.previousPage();
            }
        },

        NEXT {
            @Override
            void handle(@NotNull IPagination<?, ?> pagination) {
                pagination.nextPage();
            }
        };

        abstract void handle(@NotNull IPagination<?, ?> pagination);
    }
}