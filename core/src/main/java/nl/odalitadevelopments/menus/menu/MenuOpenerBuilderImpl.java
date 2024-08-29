package nl.odalitadevelopments.menus.menu;

import lombok.AccessLevel;
import lombok.Getter;
import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.identity.IdentityKey;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.providers.MenuProviderLoader;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.utils.BukkitThreadHelper;
import nl.odalitadevelopments.menus.utils.Pair;
import nl.odalitadevelopments.menus.utils.Triple;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter(AccessLevel.PACKAGE)
final class MenuOpenerBuilderImpl<P extends MenuProvider> implements MenuOpenerBuilder {

    @Getter(AccessLevel.NONE)
    private final MenuProcessor menuProcessor;
    @Getter(AccessLevel.NONE)
    private final ItemProcessor itemProcessor;
    @Getter(AccessLevel.NONE)
    private final SupportedMenuTypes supportedMenuTypes;

    private final P provider;
    private final Player player;
    private final MenuProviderLoader<P> providerLoader;

    private Identity<?> identity;
    private final Map<String, Integer> paginationPages = new HashMap<>();
    private final Map<String, Pair<Integer, Integer>> scrollableAxes = new HashMap<>();

    MenuOpenerBuilderImpl(MenuProcessor menuProcessor, ItemProcessor itemProcessor, SupportedMenuTypes supportedMenuTypes,
                          P provider, Player player, MenuProviderLoader<P> providerLoader) {
        this.menuProcessor = menuProcessor;
        this.itemProcessor = itemProcessor;
        this.supportedMenuTypes = supportedMenuTypes;
        this.provider = provider;
        this.player = player;
        this.providerLoader = providerLoader;
    }

    @Override
    public @NotNull <T> MenuOpenerBuilder identity(@NotNull IdentityKey<T> key, @NotNull T identity) {
        this.identity = new Identity<>(key, identity);
        return this;
    }

    @Override
    public @NotNull MenuOpenerBuilder pagination(@NotNull String id, int page) {
        this.paginationPages.put(id, page);
        return this;
    }

    @Override
    public @NotNull MenuOpenerBuilder paginationPages(@NotNull Collection<@NotNull Pair<@NotNull String, @NotNull Integer>> paginationPages) {
        for (Pair<String, Integer> page : paginationPages) {
            this.paginationPages.put(page.getKey(), page.getValue());
        }

        return this;
    }

    @Override
    public @NotNull MenuOpenerBuilder paginationPages(@NotNull Supplier<@NotNull Collection<@NotNull Pair<@NotNull String, @NotNull Integer>>> paginationPages) {
        return this.paginationPages(paginationPages.get());
    }

    @Override
    public @NotNull MenuOpenerBuilder scrollable(@NotNull String id, int xAxis, int yAxis) {
        this.scrollableAxes.put(id, Pair.of(xAxis, yAxis));
        return this;
    }

    @Override
    public @NotNull MenuOpenerBuilder scrollableAxes(@NotNull Collection<@NotNull Triple<@NotNull String, @NotNull Integer, Integer>> scrollableAxes) {
        for (Triple<String, Integer, Integer> axis : scrollableAxes) {
            this.scrollableAxes.put(axis.getFirst(), Pair.of(axis.getSecond(), axis.getThird()));
        }

        return this;
    }

    @Override
    public @NotNull MenuOpenerBuilder scrollableAxes(@NotNull Supplier<@NotNull Collection<@NotNull Triple<@NotNull String, @NotNull Integer, Integer>>> scrollableAxes) {
        return this.scrollableAxes(scrollableAxes.get());
    }

    @Override
    public void open() {
        BukkitThreadHelper.runSync(this.menuProcessor.getInstance().getJavaPlugin(), () -> {
            new MenuInitializer<>(this.menuProcessor, this.itemProcessor, this.supportedMenuTypes, this)
                    .initializeMenu();
        });
    }
}