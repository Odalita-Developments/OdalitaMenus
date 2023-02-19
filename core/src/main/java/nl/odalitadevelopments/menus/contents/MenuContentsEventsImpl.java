package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

record MenuContentsEventsImpl(MenuContentsImpl inventoryContents) implements MenuContentsEvents {

    @Override
    public void onPlaceableItemClick(@NotNull PlaceableItemClickAction action) {
        if (this.inventoryContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Placeable items are not supported in frames.");
        }

        this.inventoryContents.cache.setPlaceableItemClickAction(action);
    }

    @Override
    public void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action) {
        if (this.inventoryContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Placeable items are not supported in frames.");
        }

        this.inventoryContents.cache.setPlaceableItemDragAction(action);
    }

    @Override
    public void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer) {
        if (this.inventoryContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Player inventory click event is not supported in frames.");
        }

        this.inventoryContents.cache.setPlayerInventoryClickAction(eventConsumer);
    }
}