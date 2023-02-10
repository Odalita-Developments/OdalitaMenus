package nl.odalitadevelopments.menus.menu;

import nl.odalitadevelopments.menus.annotations.Menu;
import nl.odalitadevelopments.menus.contents.InventoryContents;
import nl.odalitadevelopments.menus.items.ItemProcessor;
import nl.odalitadevelopments.menus.menu.providers.MenuProvider;
import nl.odalitadevelopments.menus.menu.type.MenuType;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuTypes;
import nl.odalitadevelopments.menus.pagination.Pagination;
import nl.odalitadevelopments.menus.providers.providers.ColorProvider;
import nl.odalitadevelopments.menus.scrollable.Scrollable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

record MenuInitializer<P extends MenuProvider>(MenuProcessor menuProcessor, ItemProcessor itemProcessor,
                                               SupportedMenuTypes supportedMenuTypes,
                                               MenuOpenerBuilderImpl<P> builder) {

    void initializeMenu() {
        P menuProvider = this.builder.getProvider();
        Player player = this.builder.getPlayer();

        try {
            Menu annotation = menuProvider.getClass().getAnnotation(Menu.class);

            MenuType menuType;
            if ((menuType = this.supportedMenuTypes.getSupportedMenuType(annotation.type())) == null) {
                throw new IllegalArgumentException("Inventory type not supported: '" + annotation.type() + "'");
            }

            Inventory inventory;
            ColorProvider colorProvider = this.menuProcessor.getInstance().getProvidersContainer().getColorProvider();
            String inventoryTitle = colorProvider.handle(annotation.title());
            if (menuType.type() == InventoryType.CHEST) {
                inventory = Bukkit.createInventory(null, annotation.rows() * 9, inventoryTitle);
            } else {
                inventory = Bukkit.createInventory(null, menuType.type(), inventoryTitle);
            }

            MenuSession menuSession = new MenuSession(this.menuProcessor.getInstance(), player, menuType, annotation.rows(), inventory, annotation.title(), annotation.globalCacheKey());

            InventoryContents contents = menuSession.getInventoryContents();
            this.builder.getProviderLoader().load(menuProvider, player, contents);

            this.builder.getPaginationPages().forEach((id, page) -> {
                Pagination pagination = menuSession.getCache().getPaginationMap().get(id);
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
            menuSession.setOpened(true);

            menuSession.getActionsAfterOpening().removeIf((action) -> {
                action.run();
                return true;
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openInventory(@NotNull Player player, @NotNull MenuSession menuSession) {
        player.openInventory(menuSession.getInventory());
        this.menuProcessor.getOpenMenus().put(player, menuSession);
    }
}