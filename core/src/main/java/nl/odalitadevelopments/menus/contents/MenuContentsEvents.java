package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuCloseResult;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public sealed interface MenuContentsEvents permits MenuContentsEventsImpl {

    void onPlaceableItemClick(@NotNull PlaceableItemClickAction action);

    void onPlaceableItemShiftClick(@NotNull PlaceableItemShiftClickAction action);

    void onPlaceableItemDrag(@NotNull PlaceableItemDragAction action);

    void onPlayerInventoryClick(@NotNull Consumer<@NotNull InventoryClickEvent> eventConsumer);

    void onClose(boolean beforeUnregisteringMenu, @NotNull Supplier<@NotNull MenuCloseResult> action);

    void onClose(@NotNull Supplier<@NotNull MenuCloseResult> action);

    void onClose(boolean beforeUnregisteringMenu, @NotNull Runnable action);

    void onClose(@NotNull Runnable action);
}