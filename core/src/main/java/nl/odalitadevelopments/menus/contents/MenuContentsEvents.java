package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuCloseResult;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import nl.odalitadevelopments.menus.listeners.OdalitaEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
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

    <T extends InventoryEvent> @NotNull OdalitaEventListener onInventoryEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer, @NotNull EventPriority priority, boolean ignoreCancelled);

    <T extends InventoryEvent> @NotNull OdalitaEventListener onInventoryEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer, @NotNull EventPriority priority);

    <T extends InventoryEvent> @NotNull OdalitaEventListener onInventoryEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer);

    <T extends PlayerEvent> @NotNull OdalitaEventListener onPlayerEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer, @NotNull EventPriority priority, boolean ignoreCancelled);

    <T extends PlayerEvent> @NotNull OdalitaEventListener onPlayerEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer, @NotNull EventPriority priority);

    <T extends PlayerEvent> @NotNull OdalitaEventListener onPlayerEvent(@NotNull Class<? extends T> eventClass, @NotNull Consumer<@NotNull T> eventConsumer);
}