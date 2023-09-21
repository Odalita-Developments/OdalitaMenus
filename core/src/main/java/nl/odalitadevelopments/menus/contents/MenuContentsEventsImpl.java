package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
final class MenuContentsEventsImpl implements MenuContentsEvents {

    private final MenuContentsImpl menuContents;

    @Override
    public void onPlaceableItemClick(@NotNull PlaceableItemClickAction action) {
        this.menuContents.cache.setPlaceableItemClickAction(action);
    }

    @Override
    public void onPlaceableItemShiftClick(@NotNull PlaceableItemShiftClickAction action) {
        this.menuContents.cache.setPlaceableItemShiftClickAction(action);
    }

    @Override
    public void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action) {
        this.menuContents.cache.setPlaceableItemDragAction(action);
    }

    @Override
    public void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer) {
        this.menuContents.cache.setPlayerInventoryClickAction(eventConsumer);
    }

    @Override
    public void onClose(boolean beforePlaceableItemRemoveAction, @NotNull Runnable action) {
        if (beforePlaceableItemRemoveAction) {
            this.menuContents.cache.setCloseActionBefore(action);
        } else {
            this.menuContents.cache.setCloseActionAfter(action);
        }
    }

    @Override
    public void onClose(@NotNull Runnable action) {
        this.onClose(true, action);
    }
}