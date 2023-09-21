package nl.odalitadevelopments.menus.menu;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@AllArgsConstructor
final class MenuInitializer<P extends MenuProvider> {

    private final MenuProcessor menuProcessor;
    private final ItemProcessor itemProcessor;
    private final SupportedMenuTypes supportedMenuTypes;
    private final MenuOpenerBuilderImpl<P> builder;

    void initializeMenu() {
        P menuProvider = this.builder.getProvider();
        Player player = this.builder.getPlayer();

        try {
            Menu annotation = menuProvider.getClass().getAnnotation(Menu.class);

            ColorProvider colorProvider = this.menuProcessor.getInstance().getProvidersContainer().getColorProvider();
            String inventoryTitle = colorProvider.handle(annotation.title());

            SupportedMenuType menuType = this.supportedMenuTypes.getSupportedMenuType(annotation.type());
            Inventory inventory = menuType.createInventory(inventoryTitle);

            String menuId = (annotation.id().isEmpty() || annotation.id().isBlank()) ? null : annotation.id();
            MenuSession menuSession = new MenuSession(this.menuProcessor.getInstance(), menuId, menuType, inventory, annotation, false); // TODO
            menuSession.getViewers().add(player);

            IMenuContents contents = menuSession.getMenuContents();
            this.builder.getProviderLoader().load(menuProvider, player, contents);
            menuSession.initialized();

            this.builder.getPaginationPages().forEach((id, page) -> {
                IPagination<?, ?> pagination = menuSession.getCache().getPaginationMap().get(id);
                if (pagination == null) return;

                pagination.setPage(page);
            });

            this.builder.getScrollableAxes().forEach((id, axes) -> {
                Scrollable scrollable = menuSession.getCache().getScrollableMap().get(id);
                if (scrollable == null) return;

                scrollable.setAxes(axes.getKey(), axes.getValue());
            });

            this.itemProcessor.initializeItems(menuSession, contents);

            this.openInventory(player, menuSession);

            Bukkit.getScheduler().runTaskLater(this.menuProcessor.getInstance().getJavaPlugin(), menuSession::opened, 1L);
        } catch (Exception exception) {
            this.menuProcessor.getInstance().getJavaPlugin().getLogger().log(Level.SEVERE, "An error occurred while initializing a menu for " + player.getName() + "!", exception);
        }
    }

    private void openInventory(@NotNull Player player, @NotNull MenuSession menuSession) {
        this.menuProcessor.getOpenMenus().put(player, menuSession);
        player.openInventory(menuSession.getInventory());
    }
}