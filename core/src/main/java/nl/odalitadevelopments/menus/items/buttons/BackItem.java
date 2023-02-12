package nl.odalitadevelopments.menus.items.buttons;

import nl.odalitadevelopments.menus.OdalitaMenus;
import nl.odalitadevelopments.menus.menu.MenuOpenerBuilder;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class BackItem<P extends MenuProvider> extends OpenMenuItem<P> {

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider,
                                                                   @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction,
                                                                   @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new BackItem<>(itemStack, menuProvider, builderFunction, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider,
                                                                   @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction) {
        return new BackItem<>(itemStack, menuProvider, builderFunction);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider, @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new BackItem<>(itemStack, menuProvider, (builder) -> builder, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull ItemStack itemStack, @NotNull P menuProvider) {
        return new BackItem<>(itemStack, menuProvider, (builder) -> builder);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull P menuProvider,
                                                                   @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction,
                                                                   @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new BackItem<>(menuProvider, builderFunction, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull P menuProvider,
                                                                   @NotNull Function<@NotNull MenuOpenerBuilder, @NotNull MenuOpenerBuilder> builderFunction) {
        return new BackItem<>(menuProvider, builderFunction);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull P menuProvider, @NotNull MenuProviderLoader<P> menuProviderLoader) {
        return new BackItem<>(menuProvider, (builder) -> builder, menuProviderLoader);
    }

    public static <P extends MenuProvider> @NotNull BackItem<P> of(@NotNull P menuProvider) {
        return new BackItem<>(menuProvider, (builder) -> builder);
    }

    private BackItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction, MenuProviderLoader<P> menuProviderLoader) {
        super(itemStack, menuProvider, builderFunction, menuProviderLoader);
    }

    private BackItem(ItemStack itemStack, P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        super(itemStack, menuProvider, builderFunction);
    }

    private BackItem(P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction, MenuProviderLoader<P> menuProviderLoader) {
        super(menuProvider, builderFunction, menuProviderLoader);
    }

    private BackItem(P menuProvider, Function<MenuOpenerBuilder, MenuOpenerBuilder> builderFunction) {
        super(menuProvider, builderFunction);
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull OdalitaMenus instance) {
        if (super.itemStack == null) {
            super.itemStack = instance.getProvidersContainer().getDefaultItemProvider().backItem(super.menuProvider);
        }

        return super.itemStack;
    }
}