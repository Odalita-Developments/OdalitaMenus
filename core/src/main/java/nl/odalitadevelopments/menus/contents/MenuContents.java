package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPlaceableItemContents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuContents
        extends IMenuContents, IFrameContents, IPaginationScrollableContents, IPlaceableItemContents
        permits MenuContentsImpl {

    @Contract("_ -> new")
    @ApiStatus.Internal
    static @NotNull MenuContents create(@NotNull MenuSession menuSession) {
        return new MenuContentsImpl(menuSession);
    }

    @NotNull
    MenuSession menuSession();

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsActions actions();

    @NotNull
    MenuContentsEvents events();
}