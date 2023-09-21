package nl.odalitadevelopments.menus.contents.interfaces;

import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IFrameContents {

    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, @NotNull SlotPos slotPos, @NotNull Class<F> frame);

    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int row, int column, @NotNull Class<F> frame);

    <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int slot, @NotNull Class<F> frame);

    boolean loadFrame(@NotNull String id, Object @NotNull ... arguments);

    void unloadFrame(@NotNull String id);

    void registerFrameOverlaySlots(SlotPos @NotNull ... slots);

    void registerFrameOverlaySlots(int... slots);

    @Nullable String loadedFrameId();
}