package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.annotations.MenuFrame;
import nl.odalitadevelopments.menus.contents.frame.MenuFrameData;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.pos.SlotPos;
import nl.odalitadevelopments.menus.menu.AbstractMenuSession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProvider;
import nl.odalitadevelopments.menus.menu.providers.frame.MenuFrameProviderLoader;
import nl.odalitadevelopments.menus.nms.OdalitaMenusNMS;
import nl.odalitadevelopments.menus.utils.cooldown.Cooldown;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Arrays;

interface IFrameContentsImpl extends IFrameContents {

    AbstractMenuSession<?, ?, ?> menuSession();

    @NotNull
    MenuSessionCache cache();

    @Override
    default <F extends MenuFrameProvider> void registerFrame(@NotNull String id, @NotNull SlotPos slotPos, @NotNull Class<F> frameClass) {
        this.registerFrame(id, slotPos.getRow(), slotPos.getColumn(), frameClass);
    }

    @Override
    default <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int row, int column, @NotNull Class<F> frameClass) {
        if (!frameClass.isAnnotationPresent(MenuFrame.class)) {
            throw new IllegalArgumentException("The frameClass class '" + frameClass.getName() + "' is not annotated with @MenuFrame!");
        }

        MenuFrame frameData = frameClass.getAnnotation(MenuFrame.class);

        MenuFrameData previousValue = this.cache().getFrames().putIfAbsent(id, new MenuFrameData(id, frameData.height(), frameData.width(), row, column, frameClass));
        if (previousValue != null) {
            throw new IllegalArgumentException("The frameClass with the id '" + id + "' is already registered!");
        }
    }

    @Override
    default <F extends MenuFrameProvider> void registerFrame(@NotNull String id, int slot, @NotNull Class<F> frameClass) {
        this.registerFrame(id, SlotPos.of(slot), frameClass);
    }

    @Override
    default boolean loadFrame(@NotNull String id, Object @NotNull ... arguments) {
        if (this.menuSession().isClosed()) return false;

        MenuFrameData frameData = this.cache().getFrames().get(id);
        if (frameData == null) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not registered!");
        }

        Cooldown cooldown = this.menuSession().instance().getProvidersContainer().getCooldownProvider().frameLoadCooldown();
        if (cooldown != null && this.menuSession().instance().getCooldownContainer().checkAndCreate(this.menuSession(), "INTERNAL_FRAME_LOAD_COOLDOWN", cooldown)) {
            return false;
        }

        if (this.cache().getLoadedFrameId() != null) {
            this.unloadFrame(this.cache().getLoadedFrameId());
        }

        Class<? extends MenuFrameProvider> frameClass = frameData.frameClass();
        MenuFrameProvider frame;
        try {
            Constructor<?> constructor;
            if (arguments.length == 0) {
                constructor = frameClass.getDeclaredConstructor();
            } else {
                Class<?>[] parameterTypes = Arrays.stream(arguments)
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);

                constructor = frameClass.getDeclaredConstructor(parameterTypes);
            }

            constructor.setAccessible(true);
            frame = (MenuFrameProvider) constructor.newInstance(arguments);
        } catch (Exception exception) {
            throw new IllegalArgumentException("The frameClass '" + frameClass.getName() + "' does not have a constructor with the arguments: " + Arrays.toString(arguments), exception);
        }

        MenuFrameProviderLoader<MenuFrameProvider> loader = this.menuSession().instance().getMenuProcessor().getMenuFrameProcessor().getFrameProviderLoader(frame);
        MenuFrameContentsImpl frameContents = new MenuFrameContentsImpl(this.menuSession(), frameData);
        // TODO        loader.load(frame, this.menuSession.getViewer(), frameContents);

        this.cache().setLoadedFrameId(id);
        return true;
    }

    @Override
    default void unloadFrame(@NotNull String id) {
        if (this.menuSession().isClosed()) return;

        MenuFrameData frameData = this.cache().getFrames().get(id);
        if (frameData == null) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not registered!");
        }

        if (!id.equals(this.cache().getLoadedFrameId())) {
            throw new IllegalArgumentException("The frame with the id '" + id + "' is not loaded!");
        }

        for (int row = frameData.startRow(); row < Math.min(this.menuSession().rows(), frameData.startRow() + frameData.height()); row++) {
            for (int column = frameData.startColumn(); column < Math.min(this.menuSession().columns(), frameData.startColumn() + frameData.width()); column++) {
                int slot = SlotPos.of(row, column).getSlot();
                if (this.cache().getFrameOverlaySlots().contains(slot)) continue;

                this.menuSession().contents()[row][column] = null;

                this.cache().getRefreshableItems().remove(slot);
                this.cache().getPageSwitchUpdateItems().remove(slot);

                OdalitaMenusNMS.getInstance().setInventoryItem(slot, null, this.menuSession().inventory());
            }
        }

        this.cache().setLoadedFrameId(null);
    }

    @Override
    default void registerFrameOverlaySlots(SlotPos @NotNull ... slots) {
        for (SlotPos slot : slots) {
            this.registerFrameOverlaySlots(slot.getSlot());
        }
    }

    @Override
    default void registerFrameOverlaySlots(int... slots) {
        for (int slot : slots) {
            this.cache().getFrameOverlaySlots().add(slot);
        }
    }

    @Override
    default @Nullable String loadedFrameId() {
        return this.cache().getLoadedFrameId();
    }
}