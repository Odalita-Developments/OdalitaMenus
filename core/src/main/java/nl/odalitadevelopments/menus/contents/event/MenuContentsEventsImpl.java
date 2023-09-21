package nl.odalitadevelopments.menus.contents.event;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
final class MenuContentsEventsImpl implements MenuContentsEvents {

    private final MenuSessionCache cache;

    @Override
    public void onPlaceableItemClick(@NotNull PlaceableItemClickAction action) {
        this.cache.setPlaceableItemClickAction(action);
    }

    @Override
    public void onPlaceableItemShiftClick(@NotNull PlaceableItemShiftClickAction action) {
        this.cache.setPlaceableItemShiftClickAction(action);
    }

    @Override
    public void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action) {
        this.cache.setPlaceableItemDragAction(action);
    }

    @Override
    public void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer) {
        this.cache.setPlayerInventoryClickAction(eventConsumer);
    }

    @Override
    public void onClose(boolean beforePlaceableItemRemoveAction, @NotNull Runnable action) {
        if (beforePlaceableItemRemoveAction) {
            this.cache.setCloseActionBefore(action);
        } else {
            this.cache.setCloseActionAfter(action);
        }
    }

    @Override
    public void onClose(@NotNull Runnable action) {
        this.onClose(true, action);
    }
}