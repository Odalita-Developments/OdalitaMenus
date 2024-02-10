package nl.odalitadevelopments.menus.menu;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.MenuContents;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.pagination.IPagination;
import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import nl.odalitadevelopments.menus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
            Object nmsInventory = InventoryUtils.openAnvilInventory(player);
            Inventory inventory = InventoryUtils.getInventoryFromNMSMenu(nmsInventory); /*menuType.createInventory(inventoryTitle);*/

            String menuId = (annotation.id().isEmpty() || annotation.id().isBlank()) ? null : annotation.id();
            MenuSession menuSession = new MenuSession(this.menuProcessor.getInstance(), player, menuId, menuType, inventory, inventoryTitle, annotation.globalCacheKey());

            MenuContents contents = menuSession.getMenuContents();
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

            this.openInventory(player, nmsInventory, menuSession);

            Bukkit.getScheduler().runTaskLater(this.menuProcessor.getInstance().getJavaPlugin(), menuSession::opened, 1L);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openInventory(Player player, Object nmsInventory, MenuSession menuSession) {
        this.menuProcessor.getOpenMenus().put(player, menuSession);
        InventoryUtils.openInventory(player, nmsInventory, menuSession.getTitle());
    }
}