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
            if ((menuType = this.supportedMenuTypes.getSupportedMenuType(annotation.inventoryType())) == null) {
                throw new IllegalArgumentException("Inventory type not supported: '" + annotation.inventoryType() + "'");
            }

            MenuObject menuObject = new MenuObject(player, menuType, annotation.rows(), annotation.displayName());

            InventoryContents contents = InventoryContents.create(menuObject);
            this.builder.getProviderLoader().load(menuProvider, player, contents);

            this.builder.getPaginationPages().forEach((id, page) -> {
                Pagination pagination = menuObject.getPaginationMap().get(id);
                if (pagination == null) return;

                pagination.setCurrentPage(page);
            });

            this.builder.getScrollableAxes().forEach((id, axes) -> {
                Scrollable scrollable = menuObject.getScrollableMap().get(id);
                if (scrollable == null) return;

                scrollable.setAxes(axes.getKey(), axes.getValue());
            });

            this.itemProcessor.initializeItems(menuObject, contents);

            this.openInventory(player, menuObject);
        } catch (Exception exception) {
            exception.printStackTrace();
            // TODO idk, something else
        }
    }

    private void openInventory(@NotNull Player player, @NotNull MenuObject menuObject) {
        player.openInventory(menuObject.getInventory());
        this.menuProcessor.getOpenMenus().put(player, menuObject);
    }
}