package nl.odalitadevelopments.menus.contents;

import nl.odalitadevelopments.menus.contents.action.MenuContentsActions;
import nl.odalitadevelopments.menus.contents.event.MenuContentsEvents;
import nl.odalitadevelopments.menus.contents.scheduler.MenuContentsScheduler;
import nl.odalitadevelopments.menus.identity.Identity;
import nl.odalitadevelopments.menus.menu.MenuIdentitySession;
import nl.odalitadevelopments.menus.menu.cache.MenuSessionCache;
import org.jetbrains.annotations.NotNull;

final class MenuIdentityContentsImpl<T> implements MenuIdentityContents<T>, IMenuContentsImpl, IPaginationScrollableContentsImpl, IFrameContentsImpl {

    private final MenuIdentitySession menuSession;
    private final MenuContentsScheduler scheduler;
    private final MenuContentsActions actions;
    private final MenuContentsEvents events;

    private final Identity<T> identity;

    MenuIdentityContentsImpl(MenuIdentitySession menuSession, Identity<T> identity) {
        this.menuSession = menuSession;

        this.scheduler = MenuContentsScheduler.create(menuSession.cache());
        this.actions = MenuContentsActions.create(menuSession);
        this.events = MenuContentsEvents.create(menuSession);

        this.identity = identity;
    }

    @Override
    public @NotNull MenuIdentitySession menuSession() {
        return this.menuSession;
    }

    @Override
    public @NotNull MenuContentsScheduler scheduler() {
        return this.scheduler;
    }

    @Override
    public @NotNull MenuContentsActions actions() {
        return this.actions;
    }

    @Override
    public @NotNull MenuContentsEvents events() {
        return this.events;
    }

    @Override
    public @NotNull MenuSessionCache cache() {
        return this.menuSession.cache();
    }

    @Override
    public @NotNull Identity<T> identity() {
        return this.identity;
    }
}