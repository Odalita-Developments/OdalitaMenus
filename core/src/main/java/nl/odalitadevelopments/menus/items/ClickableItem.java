package nl.odalitadevelopments.menus.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClickableItem extends MenuItem {

    public static @NotNull ClickableItem of(@NotNull ItemStack itemStack, @NotNull Consumer<@NotNull InventoryClickEvent> clickHandler) {
        return new ClickableItem(itemStack, clickHandler);
    }

    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> clickHandler;

    @Override
    protected @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance, @NotNull MenuContents contents) {
        return this.clickHandler;
    }
}