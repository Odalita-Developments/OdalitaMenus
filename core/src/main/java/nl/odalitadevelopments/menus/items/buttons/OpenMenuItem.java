package nl.odalitadevelopments.menus.items.buttons;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class OpenMenuItem<P extends MenuProvider> extends MenuItem {

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider,
                                                                       @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction) {
        return new OpenMenuItem<>(itemStack, menuProvider, builderFunction);
    }

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider) {
        return new OpenMenuItem<>(itemStack, menuProvider, (builder) -> builder);
    }

    protected final P menuProvider;
    protected final Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction;

    protected ItemStack itemStack;

    protected OpenMenuItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        this.menuProvider = menuProvider;
        this.builderFunction = builderFunction;
        this.itemStack = itemStack;
    }

    protected OpenMenuItem(P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        this(null, menuProvider, builderFunction);
    }

    @Override
    protected @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance, @NotNull AbstractMenuSession<?, ?, ?> menuSession) {
        if (this.itemStack == null) {
            this.itemStack = new ItemStack(Material.AIR);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance, @NotNull AbstractMenuSession<?, ?, ?> menuSession) {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            MenuProcessor menuProcessor = instance.getMenuProcessor();
            MenuOpenerBuilder builder = menuProcessor.openMenuBuilder(this.menuProvider, player);

            this.builderFunction.apply(builder)
                    .open();
        };
    }
}