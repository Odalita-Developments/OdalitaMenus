package nl.odalitadevelopments.menus.contents;

import lombok.AllArgsConstructor;
import nl.odalitadevelopments.menus.contents.action.MenuCloseResult;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemClickAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemDragAction;
import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemShiftClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

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
    public void onPlaceableItemShiftClick(@NotNull PlaceableItemShiftClickAction action) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Placeable items are not supported in frames.");
        }

        this.menuContents.cache.setPlaceableItemShiftClickAction(action);
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
    public void onClose(boolean beforeUnregisteringMenu, @NotNull Supplier<@NotNull MenuCloseResult> action) {
        if (this.menuContents.menuFrameData() != null) {
            throw new UnsupportedOperationException("Close event is not supported in frames.");
        }

        if (beforeUnregisteringMenu) {
            this.menuContents.cache.setCloseActionBefore(action);
        } else {
            this.menuContents.cache.setCloseActionAfter(action);
        }
    }

    @Override
    public void onClose(@NotNull Supplier<@NotNull MenuCloseResult> action) {
        this.onClose(true, action);
    }

    @Override
    public void onClose(boolean beforeUnregisteringMenu, @NotNull Runnable action) {
        this.onClose(beforeUnregisteringMenu, () -> {
            action.run();
            return MenuCloseResult.CLOSE;
        });
    }

    @Override
    public void onClose(@NotNull Runnable action) {
        this.onClose(true, action);
    }
}