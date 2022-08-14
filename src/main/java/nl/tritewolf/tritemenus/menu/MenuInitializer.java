package nl.tritewolf.tritemenus.menu;

import nl.tritewolf.tritemenus.annotations.Menu;
import nl.tritewolf.tritemenus.contents.InventoryContents;
import nl.tritewolf.tritemenus.items.ItemProcessor;
import nl.tritewolf.tritemenus.menu.providers.MenuProvider;
import nl.tritewolf.tritemenus.menu.type.MenuType;
import nl.tritewolf.tritemenus.menu.type.SupportedMenuTypes;
import nl.tritewolf.tritemenus.pagination.Pagination;
import nl.tritewolf.tritemenus.scrollable.Scrollable;
import org.bukkit.entity.Player;
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

            MenuSession menuSession = new MenuSession(player, menuType, annotation.rows(), annotation.title());

            InventoryContents contents = InventoryContents.create(menuSession);
            this.builder.getProviderLoader().load(menuProvider, player, contents);

            this.builder.getPaginationPages().forEach((id, page) -> {
                Pagination pagination = menuSession.getCache().getPaginationMap().get(id);
                if (pagination == null) return;

                pagination.setCurrentPage(page);
            });

            this.builder.getScrollableAxes().forEach((id, axes) -> {
                Scrollable scrollable = menuSession.getCache().getScrollableMap().get(id);
                if (scrollable == null) return;

                scrollable.setAxes(axes.getKey(), axes.getValue());
            });

            this.itemProcessor.initializeItems(menuSession, contents);

            this.openInventory(player, menuSession);
        } catch (Exception exception) {
            exception.printStackTrace();
            // TODO idk, something else
        }
    }

    private void openInventory(@NotNull Player player, @NotNull MenuSession menuSession) {
        player.openInventory(menuSession.getInventory());
        this.menuProcessor.getOpenMenus().put(player, menuSession);
    }
}