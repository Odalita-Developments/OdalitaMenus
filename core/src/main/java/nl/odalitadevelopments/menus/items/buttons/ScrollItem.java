package nl.odalitadevelopments.menus.items.buttons;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.providers.providers.DefaultItemProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class ScrollItem extends PageUpdatableItem {

    public static @NotNull ScrollItem up(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new ScrollItem(Direction.UP, scrollable, itemStack, showOnLastPage);
    }

    public static @NotNull ScrollItem up(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack) {
        return new ScrollItem(Direction.UP, scrollable, itemStack, false);
    }

    public static @NotNull ScrollItem up(@NotNull Scrollable scrollable, boolean showOnLastPage) {
        return new ScrollItem(Direction.UP, scrollable, showOnLastPage);
    }

    public static @NotNull ScrollItem up(@NotNull Scrollable scrollable) {
        return new ScrollItem(Direction.UP, scrollable, false);
    }

    public static @NotNull ScrollItem down(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new ScrollItem(Direction.DOWN, scrollable, itemStack, showOnLastPage);
    }

    public static @NotNull ScrollItem down(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack) {
        return new ScrollItem(Direction.DOWN, scrollable, itemStack, false);
    }

    public static @NotNull ScrollItem down(@NotNull Scrollable scrollable, boolean showOnLastPage) {
        return new ScrollItem(Direction.DOWN, scrollable, showOnLastPage);
    }

    public static @NotNull ScrollItem down(@NotNull Scrollable scrollable) {
        return new ScrollItem(Direction.DOWN, scrollable, false);
    }

    public static @NotNull ScrollItem left(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new ScrollItem(Direction.LEFT, scrollable, itemStack, showOnLastPage);
    }

    public static @NotNull ScrollItem left(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack) {
        return new ScrollItem(Direction.LEFT, scrollable, itemStack, false);
    }

    public static @NotNull ScrollItem left(@NotNull Scrollable scrollable, boolean showOnLastPage) {
        return new ScrollItem(Direction.LEFT, scrollable, showOnLastPage);
    }

    public static @NotNull ScrollItem left(@NotNull Scrollable scrollable) {
        return new ScrollItem(Direction.LEFT, scrollable, false);
    }

    public static @NotNull ScrollItem right(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack, boolean showOnLastPage) {
        return new ScrollItem(Direction.RIGHT, scrollable, itemStack, showOnLastPage);
    }

    public static @NotNull ScrollItem right(@NotNull Scrollable scrollable, @NotNull ItemStack itemStack) {
        return new ScrollItem(Direction.RIGHT, scrollable, itemStack, false);
    }

    public static @NotNull ScrollItem right(@NotNull Scrollable scrollable, boolean showOnLastPage) {
        return new ScrollItem(Direction.RIGHT, scrollable, showOnLastPage);
    }

    public static @NotNull ScrollItem right(@NotNull Scrollable scrollable) {
        return new ScrollItem(Direction.RIGHT, scrollable, false);
    }

    private final Direction direction;
    private final Scrollable scrollable;
    private final boolean showOnLastPage;

    private ItemStack itemStack;

    private ScrollItem(Direction direction, Scrollable scrollable, ItemStack itemStack, boolean showOnLastPage) {
        this.direction = direction;
        this.scrollable = scrollable;
        this.itemStack = itemStack;
        this.showOnLastPage = showOnLastPage;
    }

    private ScrollItem(Direction direction, Scrollable scrollable, boolean showOnLastPage) {
        this(direction, scrollable, null, showOnLastPage);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance) {
        if (!this.showOnLastPage && this.isOnLastPageForDirection()) {
            return new ItemStack(Material.AIR);
        }

        if (this.itemStack == null) {
            DefaultItemProvider defaultItemProvider = instance.getProvidersContainer().getDefaultItemProvider();
            return switch (this.direction) {
                case UP -> defaultItemProvider.scrollUpItem(this.scrollable);
                case DOWN -> defaultItemProvider.scrollDownItem(this.scrollable);
                case LEFT -> defaultItemProvider.scrollLeftItem(this.scrollable);
                case RIGHT -> defaultItemProvider.scrollRightItem(this.scrollable);
            };
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance) {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            MenuSession menuSession = instance.getOpenMenuSession(player);
            if (menuSession == null) return;

            Cooldown cooldown = instance.getProvidersContainer().getCooldownProvider().scrollCooldown();
            if (cooldown != null && instance.getCooldownContainer().checkAndCreate(menuSession, "INTERNAL_SCROLL_COOLDOWN", cooldown)) {
                return;
            }

            if (!this.isOnLastPageForDirection()) {
                this.direction.next(this.scrollable);
            }
        };
    }

    private boolean isOnLastPageForDirection() {
        return (this.direction == Direction.UP && this.scrollable.isFirstVertical())
                || (this.direction == Direction.DOWN && this.scrollable.isLastVertical())
                || (this.direction == Direction.LEFT && this.scrollable.isFirstHorizontal())
                || (this.direction == Direction.RIGHT && this.scrollable.isLastHorizontal());
    }

    private enum Direction {

        UP {
            @Override
            void next(@NotNull Scrollable scrollable) {
                scrollable.previousVertical();
            }
        },

        DOWN {
            @Override
            void next(@NotNull Scrollable scrollable) {
                scrollable.nextVertical();
            }
        },

        LEFT {
            @Override
            void next(@NotNull Scrollable scrollable) {
                scrollable.previousHorizontal();
            }
        },

        RIGHT {
            @Override
            void next(@NotNull Scrollable scrollable) {
                scrollable.nextHorizontal();
            }
        };

        abstract void next(@NotNull Scrollable scrollable);
    }
}