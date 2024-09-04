package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.interfaces.IFrameContents;
import nl.odalitadevelopments.menus.contents.interfaces.IMenuContents;
import nl.odalitadevelopments.menus.contents.interfaces.IPaginationScrollableContents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.menu.MenuIdentitySession;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public sealed interface MenuIdentityContents<T>
        extends IMenuContents, IPaginationScrollableContents, IFrameContents
        permits MenuIdentityContentsImpl {

    @ApiStatus.Internal
    static <T> @NotNull MenuIdentityContents<T> create(@NotNull MenuIdentitySession menuSession, @NotNull Identity<T> identity) {
        return new MenuIdentityContentsImpl<>(menuSession, identity);
    }

    @NotNull
    MenuIdentitySession menuSession();

    @NotNull
    MenuContentsScheduler scheduler();

    @NotNull
    MenuContentsActions actions();

    @NotNull
    MenuContentsEvents events();

    @NotNull
    Identity<T> identity();
}