package nl.odalitadevelopments.menus.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class UpdatableItem extends MenuItem {

    public static @NotNull UpdatableItem of(@NotNull Supplier<@NotNull ItemStack> itemStackSupplier,
                                            @NotNull Consumer<@NotNull InventoryClickEvent> clickHandler, int updateTicks) {
        return new UpdatableItem(itemStackSupplier, clickHandler, updateTicks);
    }

    public static @NotNull UpdatableItem of(@NotNull Supplier<@NotNull ItemStack> itemStackSupplier,
                                            @NotNull Consumer<@NotNull InventoryClickEvent> clickHandler) {
        return new UpdatableItem(itemStackSupplier, clickHandler);
    }

    public static @NotNull UpdatableItem of(@NotNull Supplier<@NotNull ItemStack> itemStackSupplier, int updateTicks) {
        return new UpdatableItem(itemStackSupplier, updateTicks);
    }

    public static @NotNull UpdatableItem of(@NotNull Supplier<@NotNull ItemStack> itemStackSupplier) {
        return new UpdatableItem(itemStackSupplier);
    }

    private final Supplier<ItemStack> itemStackSupplier;
    private final Consumer<InventoryClickEvent> clickHandler;
    private final int updateTicks;

    private UpdatableItem(Supplier<ItemStack> itemStackSupplier, Consumer<InventoryClickEvent> clickHandler) {
        this(itemStackSupplier, clickHandler, 20); // 20 update ticks = 1 second
    }

    private UpdatableItem(Supplier<ItemStack> itemStackSupplier, int updateTicks) {
        this(itemStackSupplier, null, updateTicks);
    }

    private UpdatableItem(Supplier<ItemStack> itemStackSupplier) {
        this(itemStackSupplier, null); // 20 update ticks = 1 second
    }

    @Override
    protected @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
        return this.itemStackSupplier.get();
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
        return (this.clickHandler == null) ? (event) -> {} : this.clickHandler;
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public int getUpdateTicks() {
        return this.updateTicks;
    }
}