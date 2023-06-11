package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
final class MenuContentsEventsImpl implements MenuContentsEvents {

    private final MenuContentsImpl menuContents;

    @Override
    public void onPlaceableItemClick(@NotNull PlaceableItemClickAction action) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Placeable items are not supported in frames.");
        }

        this.menuContents.cache.setPlaceableItemClickAction(action);
    }

    @Override
    public void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Placeable items are not supported in frames.");
        }

        this.menuContents.cache.setPlaceableItemDragAction(action);
    }

    @Override
    public void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory click event is not supported in frames.");
        }

        this.menuContents.cache.setPlayerInventoryClickAction(eventConsumer);
    }

    @Override
    public void onClose(boolean beforePlaceableItemRemoveAction, @NotNull Runnable action) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Close event is not supported in frames.");
        }

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