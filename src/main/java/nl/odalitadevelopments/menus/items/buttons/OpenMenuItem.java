package nl.odalitadevelopments.menus.items.buttons;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.items.MenuItem;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.MenuProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class OpenMenuItem<P extends MenuProvider> implements MenuItem {

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider,
                                                                       @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction,
                                                                       @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new OpenMenuItem<>(itemStack, menuProvider, builderFunction, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider,
                                                                       @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction) {
        return new OpenMenuItem<>(itemStack, menuProvider, builderFunction);
    }

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider, @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new OpenMenuItem<>(itemStack, menuProvider, (builder) -> builder, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull OpenMenuItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider) {
        return new OpenMenuItem<>(itemStack, menuProvider, (builder) -> builder);
    }

    protected final P menuProvider;
    protected final Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction;
    protected final MenuProviderLoader<P> menuProviderLoader;

    protected ItemStack itemStack;

    protected OpenMenuItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction, MenuProviderLoader<P> menuProviderLoader) {
        this.menuProvider = menuProvider;
        this.builderFunction = builderFunction;
        this.menuProviderLoader = menuProviderLoader;
        this.itemStack = itemStack;
    }

    protected OpenMenuItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        this(itemStack, menuProvider, builderFunction, null);
    }

    protected OpenMenuItem(P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction, MenuProviderLoader<P> menuProviderLoader) {
        this(null, menuProvider, builderFunction, menuProviderLoader);
    }

    protected OpenMenuItem(P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        this(null, menuProvider, builderFunction, null);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance) {
        if (this.itemStack == null) {
            this.itemStack = new ItemStack(Material.AIR);
        }

        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick(@NotNull OdalitaMenus instance) {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            MenuProcessor menuProcessor = instance.getMenuProcessor();
            MenuOpenerBuilder builder = (this.menuProviderLoader == null)
                    ? menuProcessor.openMenuBuilder(this.menuProvider, player)
                    : menuProcessor.openMenuBuilder(this.menuProvider, player, this.menuProviderLoader);

            this.builderFunction.apply(builder)
                    .open();
        };
    }
}