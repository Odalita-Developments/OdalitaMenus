package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.menu.MenuSession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuIdentityContents<T, I extends Identity<T>>
        extends IMenuContents, IPaginationScrollableContents, IFrameContents
        permits MenuIdentityContentsImpl {

    @ApiStatus.Internal
    static <T, I extends Identity<T>> @NotNull MenuIdentityContents<T, I> create(@NotNull MenuSession menuSession, @NotNull I identity) {
        return new MenuIdentityContentsImpl<>(menuSession, identity);
    }

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsActions actions();

    @NotNull
    MenuContentsEvents events();

    @NotNull
    I identity();
}