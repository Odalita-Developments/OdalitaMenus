package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.interfaces.IDefaultContents;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuIdentityContents
        extends IMenuContents, IDefaultContents, IPaginationScrollableContents, IFrameContents
        permits MenuIdentityContentsImpl {

    @Contract("_ -> new")
    @ApiStatus.Internal
    static @NotNull MenuIdentityContents create(@NotNull MenuSession menuSession) {
        return new MenuIdentityContentsImpl(menuSession);
    }

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsActions actions();

    @NotNull
    MenuContentsEvents events();
}