package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.placeableitem.PlaceableItemsCloseAction;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.menu.MenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

final class MenuFrameContentsImpl extends MenuContentsImpl {

    private final MenuFrameData frameData;

    MenuFrameContentsImpl(MenuSession menuSession, MenuSessionCache cache, MenuFrameData frameData, MenuContentsScheduler scheduler, MenuContentsActions actions, MenuContentsEvents events) {
        super(menuSession, cache, scheduler, actions, events);

        this.frameData = frameData;
    }

    @Override
    public @Nullable MenuFrameData menuFrameData() {
        return this.frameData;
    }

    @Override
    public boolean isEmpty(@NotNull SlotPos slotPos) {
        slotPos = slotPos.convertTo(this.maxRows(), this.maxColumns());
        slotPos = slotPos.convertFromFrame(this.menuSession.getRows(), this.menuSession.getColumns(), this.frameData);
        return this.menuSession.getContent(slotPos) == null;
    }

    @Override
    public boolean isEmpty(int row, int column) {
        return this.isEmpty(SlotPos.of(
                this.maxRows(),
                this.maxColumns(),
                row,
                column
        ));
    }

    @Override
    public boolean isEmpty(int slot) {
        return this.isEmpty(SlotPos.of(
                this.maxRows(),
                this.maxColumns(),
                slot
        ));
    }

    @Override
    public void registerPlaceableItemSlots(int... slots) {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public void setForcedPlaceableItem(@NotNull SlotPos slotPos, @NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public void setForcedPlaceableItem(int row, int column, @NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public void setForcedPlaceableItem(int slot, @NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public void placeableItemsCloseAction(@NotNull PlaceableItemsCloseAction action) {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public @NotNull Optional<@NotNull SlotPos> firstEmptyPlaceableItemSlot() {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getPlaceableItems() {
        throw new UnsupportedOperationException("Placeable items are not supported in frames.");
    }

    @Override
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int slot, @NotNull Class<F> frameClass) {
        throw new UnsupportedOperationException("Cannot register a frame inside a frame");
    }

    @Override
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, @NotNull SlotPos slotPos, @NotNull Class<F> frameClass) {
        throw new UnsupportedOperationException("Cannot register a frame inside a frame");
    }

    @Override
    public <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int row, int column, @NotNull Class<F> frameClass) {
        throw new UnsupportedOperationException("Cannot register a frame inside a frame");
    }

    @Override
    public boolean loadFrame(@NotNull String id, Object @NotNull ... arguments) {
        throw new UnsupportedOperationException("Cannot load a frame inside a frame");
    }

    @Override
    public void unloadFrame(@NotNull String id) {
        throw new UnsupportedOperationException("Cannot unload a frame inside a frame");
    }

    @Override
    public void registerFrameOverlaySlots(SlotPos @NotNull ... slots) {
        throw new UnsupportedOperationException("Cannot register frame overlay slots inside a frame");
    }

    @Override
    public void registerFrameOverlaySlots(int... slots) {
        throw new UnsupportedOperationException("Cannot register frame overlay slots inside a frame");
    }

    @Override
    public @Nullable String loadedFrameId() {
        throw new UnsupportedOperationException("Cannot get loaded frame id inside a frame");
    }

    @Override
    protected SlotPos calculateSlotPos(SlotPos slotPos) {
        slotPos = slotPos.convertTo(this.maxRows(), this.maxColumns());
        return slotPos.convertFromFrame(this.menuSession.getRows(), this.menuSession.getColumns(), this.frameData);
    }

    @Override
    public int maxRows() {
        return this.frameData.height();
    }

    @Override
    public int maxColumns() {
        return this.frameData.width();
    }
}