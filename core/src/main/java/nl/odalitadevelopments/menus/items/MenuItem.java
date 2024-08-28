package nl.odalitadevelopments.menus.items;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.providers.providers.MenuItemDataProvider;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class MenuItem {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    private final int id = ID_COUNTER.get() >= 10_000 ? ID_COUNTER.getAndSet(0) : ID_COUNTER.getAndIncrement();

    protected abstract @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance, @NotNull IMenuContents contents);

    public final @NotNull ItemStack provideItem(@NotNull OdalitaMenus instance, @NotNull IMenuContents contents) {
        ItemStack itemStack = this.getItemStack(instance, contents);

        MenuItemDataProvider menuItemDataProvider = instance.getProvidersContainer().getMenuItemDataProvider();
        if (menuItemDataProvider != null) {
            itemStack = menuItemDataProvider.provideData(contents.menuSession(), itemStack);
        }

        return itemStack;
    }

    public abstract @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance, @NotNull MenuContents contents);

    public boolean isUpdatable() {
        return false;
    }

    public int getUpdateTicks() {
        return -1;
    }

    public final int getId() {
        return this.id;
    }
}