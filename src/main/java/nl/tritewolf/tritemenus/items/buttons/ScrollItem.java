package nl.tritewolf.tritemenus.items.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.items.PageUpdatableItem;
import nl.tritewolf.tritemenus.items.def.DefaultItem;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScrollItem implements PageUpdatableItem {

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
    private final ItemStack itemStack;
    private final boolean showOnLastPage;

    private ScrollItem(Direction direction, Scrollable scrollable, boolean showOnLastPage) {
        this.direction = direction;
        this.scrollable = scrollable;
        this.showOnLastPage = showOnLastPage;

        this.itemStack = switch (this.direction) {
            case UP -> DefaultItem.getItemStack(DefaultItem.SCROLL_UP);
            case DOWN -> DefaultItem.getItemStack(DefaultItem.SCROLL_DOWN);
            case LEFT -> DefaultItem.getItemStack(DefaultItem.SCROLL_LEFT);
            case RIGHT -> DefaultItem.getItemStack(DefaultItem.SCROLL_RIGHT);
        };
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        if (!this.showOnLastPage && this.isOnLastPageForDirection()) {
            return new ItemStack(Material.AIR);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player)) return;
            if (this.isOnLastPageForDirection()) return;

            this.direction.next(this.scrollable);
        };
    }

    private boolean isOnLastPageForDirection() {
        return (this.direction == Direction.UP && this.scrollable.currentVertical() == 0)
                || (this.direction == Direction.DOWN && this.scrollable.currentVertical() == this.scrollable.lastVertical())
                || (this.direction == Direction.LEFT && this.scrollable.currentHorizontal() == 0)
                || (this.direction == Direction.RIGHT && this.scrollable.currentHorizontal() == this.scrollable.lastHorizontal());
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