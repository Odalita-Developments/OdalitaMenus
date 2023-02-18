package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public sealed interface InventoryContentsEvents permits InventoryContentsEventsImpl {

    void onPlaceableItemClick(@NotNull PlaceableItemClickAction action);

    void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action);

    void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer);
}