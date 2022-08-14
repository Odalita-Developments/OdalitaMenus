package nl.tritewolf.tritemenus.items.buttons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import nl.tritewolf.tritemenus.TriteMenus;
import nl.tritewolf.tritemenus.items.MenuItem;
import nl.tritewolf.tritemenus.menu.MenuOpenerBuilder;
import nl.tritewolf.tritemenus.menu.MenuProcessor;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.providers.MenuProviderLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public sealed class OpenMenuItem<P extends MenuProvider> implements MenuItem permits BackItem {

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

    protected final ItemStack itemStack;
    protected final P menuProvider;
    protected final Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction;
    protected final MenuProviderLoader<P> menuProviderLoader;

    protected OpenMenuItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        this(itemStack, menuProvider, builderFunction, MenuProviderLoader.defaultLoader());
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public @NotNull Consumer<InventoryClickEvent> onClick() {
        return (event) -> {
            if (!(event.getWhoClicked() instanceof Player player)) return;

            MenuProcessor menuProcessor = TriteMenus.getInstance().getMenuProcessor();
            this.builderFunction.apply(menuProcessor.openMenuBuilder(this.menuProvider, player, this.menuProviderLoader))
                    .open();
        };
    }
}