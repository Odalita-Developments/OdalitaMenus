package nl.odalitadevelopments.menus.contents.event;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public sealed interface MenuContentsEvents permits MenuContentsEventsImpl {

    @Contract("_ -> new")
    @ApiStatus.Internal
    static @NotNull MenuContentsEvents create(@NotNull MenuSessionCache cache) {
        return new MenuContentsEventsImpl(cache);
    }

    void onPlaceableItemClick(@NotNull PlaceableItemClickAction action);

    void onPlaceableItemShiftClick(@NotNull PlaceableItemShiftClickAction action);

    void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action);

    void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer);

    void onClose(boolean beforePlaceableItemRemoveAction, @NotNull Runnable action);

    void onClose(@NotNull Runnable action);
}