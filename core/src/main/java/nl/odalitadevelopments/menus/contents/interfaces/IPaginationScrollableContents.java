package nl.odalitadevelopments.menus.contents.interfaces;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.items.PageUpdatableItem;
import nl.odalitadevelopments.menus.iterators.MenuIterator;
import nl.odalitadevelopments.menus.iterators.MenuObjectIterator;
import nl.odalitadevelopments.menus.pagination.PaginationBuilder;
import nl.odalitadevelopments.menus.scrollable.ScrollableBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IPaginationScrollableContents {

    @NotNull PaginationBuilder.ItemPaginationBuilder pagination(@NotNull String id, int itemsPerPage, @NotNull MenuIterator iterator);

    @NotNull <T> PaginationBuilder.ObjectPaginationBuilder<T> pagination(@NotNull String id, int itemsPerPage, @NotNull MenuObjectIterator<T> iterator);

    @NotNull PaginationBuilder pagination(@NotNull String id, int itemsPerPage);

    @NotNull ScrollableBuilder scrollable(@NotNull String id, int showYAxis, int showXAxis);

    void setPageSwitchUpdateItem(@NotNull SlotPos slotPos, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem);

    void setPageSwitchUpdateItem(int row, int column, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem);

    void setPageSwitchUpdateItem(int slot, @NotNull Supplier<@NotNull PageUpdatableItem> menuItem);
}