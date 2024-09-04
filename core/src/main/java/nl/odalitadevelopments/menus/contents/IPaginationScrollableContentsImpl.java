package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.MenuData;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.type.SupportedFeatures;
import nl.odalitadevelopments.menus.menu.type.SupportedMenuType;
import nl.odalitadevelopments.menus.pagination.PaginationBuilder;
import nl.odalitadevelopments.menus.scrollable.ScrollableBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

interface IPaginationScrollableContentsImpl extends IPaginationScrollableContents {

    AbstractMenuSession<?, ?, ?> menuSession();

    @NotNull
    MenuSessionCache cache();

    default @NotNull SupportedMenuType menuType() {
        return this.menuSession().data().getOrThrow(MenuData.Key.TYPE);
    }

    @Override
    default @NotNull PaginationBuilder.ItemPaginationBuilder pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator) {
        return this.pagination(id, itemsPerPage)
                .iterator(iterator);
    }

    @Override
    default <T> PaginationBuilder.@NotNull ObjectPaginationBuilder<T> pagination(@NotNull String id, int itemsPerPage, @NotNull MenuObjectIterator<T> iterator) {
        return this.pagination(id, itemsPerPage)
                .objectIterator(iterator);
    }

    @Override
    default @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage) {
        if (!this.menuType().isFeatureAllowed(SupportedFeatures.PAGINATION)) {
            throw new IllegalStateException("The menu type '" + this.menuType().type() + "' does not support pagination!");
        }

        return PaginationBuilder.builder(this.menuSession(), id, itemsPerPage);
    }

    @Override
    default @NotNull ScrollableBuilder scrollable(@NotNull String id, int showYAxis, int showXAxis) {
        if (!this.menuType().isFeatureAllowed(SupportedFeatures.SCROLLABLE)) {
            throw new IllegalStateException("The menu type '" + this.menuType().type() + "' does not support scrollable!");
        }

        return ScrollableBuilder.builder(this.menuSession(), id, showYAxis, showXAxis);
    }

    @Override
    default void setPageSwitchUpdateItem(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem) {
        if (!this.menuType().isFeatureAllowed(SupportedFeatures.PAGINATION)
                && !this.menuType().isFeatureAllowed(SupportedFeatures.SCROLLABLE)) {
            throw new IllegalStateException("The menu type '" + this.menuType().type() + "' does not support pagination and scrollable!");
        }

        this.menuSession().menuContents().set(slotPos, menuItem.get(), true);
        this.cache().getPageSwitchUpdateItems().put(slotPos.getSlot(), menuItem);
    }

    @Override
    default void setPageSwitchUpdateItem(int row, int column, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem) {
        this.setPageSwitchUpdateItem(SlotPos.of(row, column), menuItem);
    }

    @Override
    default void setPageSwitchUpdateItem(int slot, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem) {
        this.setPageSwitchUpdateItem(SlotPos.of(slot), menuItem);
    }
}